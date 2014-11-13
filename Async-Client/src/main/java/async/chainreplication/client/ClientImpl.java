package async.chainreplication.client;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.client.threads.ResponseListener;
import async.chainreplication.communication.messages.LogMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.master.models.Client;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import async.common.util.RequestWithChain;
import async.common.util.TestCases;
import aync.chainreplication.base.impl.ChainReplicationImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientImpl.
 */
public class ClientImpl extends ChainReplicationImpl {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		final Config config = ConfigUtil.deserializeFromFile(args[0]);
		final ClientImpl clientImpl = new ClientImpl(config, args[1]);
		clientImpl.init();
		clientImpl.stop();
	}

	/** The response wait time. */
	long responseWaitTime = 10000;

	/** The master update listener. */
	MasterUpdateListenerThread masterUpdateListener;

	/** The response listener. */
	ResponseListener responseListener;
	
	/** The request dispatcher. */
	RequestDispatcher requestDispatcher;

	/** The client chain replication facade. */
	ClientChainReplicationFacade clientChainReplicationFacade;

	/** The client id. */
	String clientId;
	
	/** The test cases. */
	TestCases testCases;

	/**
	 * Instantiates a new client impl.
	 *
	 * @param config
	 *            the config
	 * @param clientId
	 *            the client id
	 */
	public ClientImpl(Config config, String clientId) {
		super(clientId);
		this.clientId = clientId;
		try {
			clientChainReplicationFacade = new ClientChainReplicationFacade(
					config.getClients().get(clientId), config.getChains(),
					config.getMaster(), this);
			responseWaitTime = config.getClients().get(clientId)
					.getResponseWaitTime();
			this.testCases = config.getTestCases().get(config.getClients().get(clientId));
		} catch (final ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
		}
		this.logMessage("Client Started:" + config.getClients().get(clientId));
	}

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public Client getClient() {
		return clientChainReplicationFacade.getClientMessageHandler()
				.getClient();
	}
	
	

	/**
	 * Gets the client chain replication facade.
	 *
	 * @return the client chain replication facade
	 */
	public ClientChainReplicationFacade getClientChainReplicationFacade() {
		return clientChainReplicationFacade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#init()
	 */
	public void init() {
		super.init();
		try {
			masterUpdateListener = new MasterUpdateListenerThread(this);
			responseListener = new ResponseListener(this);
			requestDispatcher = new RequestDispatcher(responseWaitTime, this, this.testCases);
		} catch (final ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
			this.stop();
		}
		masterUpdateListener.start();
		responseListener.start();
		try {
			this.clientChainReplicationFacade.startProcessingMessages();
		} catch (ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
			this.stop();
		}
		this.logMessage("Client Started");
		try {
			requestDispatcher.join();
		} catch (InterruptedException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
			this.stop();
		}
	}

	/**
	 * Log message.
	 *
	 * @param message
	 *            the message
	 */
	public void logMessage(String message) {
		final LogMessage logMessage = new LogMessage(message);
		this.getLogMessages().enqueueMessageObject(
				logMessage.getPriority().ordinal(), logMessage);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#stop()
	 */
	public void stop() {
		this.logMessage("Client Stopping");
		responseListener.stopThread();
		masterUpdateListener.stopThread();
		super.stop();
	}
	
	
	/**
	 * The Class RequestDispatcher.
	 */
	public static class RequestDispatcher extends Thread {
		
		/** The client impl. */
		private ClientImpl clientImpl;
		
		/** The response wait time. */
		private long responseWaitTime;
		
		/** The test cases. */
		private TestCases testCases;
		
		/** The is running. */
		volatile boolean isRunning = false;

		/**
		 * Instantiates a new request dispatcher.
		 *
		 * @param responseWaitTime the response wait time
		 * @param clientImpl the client impl
		 * @param testCases the test cases
		 */
		public RequestDispatcher(long responseWaitTime, ClientImpl clientImpl,
				TestCases testCases) {
			this.clientImpl = clientImpl;
			this.responseWaitTime = responseWaitTime;
			this.testCases = testCases;
		}
		
		/**
		 * Checks if is running.
		 *
		 * @return true, if is running
		 */
		public boolean isRunning() {
			return isRunning;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			isRunning = true;
			try {
				performOperations();
				Thread.sleep(this.responseWaitTime);
				this.clientImpl.getClientChainReplicationFacade().stopProcessingMessages();
			} catch (ClientChainReplicationException | InterruptedException e) {
				this.clientImpl.logMessage(e.getMessage());
			}
			isRunning = false;
		}
		
		/**
		 * Perform operations.
		 *
		 * @throws ClientChainReplicationException             the client chain replication exception
		 */
		private void performOperations()
				throws ClientChainReplicationException {
			for (final RequestWithChain request : this.testCases.getRequests()) {
				final int retryCount = request.getRequest().getRetryCount();
				int retry = 0;
				while (retry <= retryCount) {
					final RequestMessage requestMessage = new RequestMessage(
							request.getRequest());
					final ClientRequestMessage clientRequestMessage = new ClientRequestMessage(
							request.getChainName(), requestMessage);
					this.clientImpl.getClientChainReplicationFacade().deliverMessage(clientRequestMessage);
					try {
						Thread.sleep(this.responseWaitTime);
					} catch (final InterruptedException e) {
						throw new ClientChainReplicationException(e);
					}
					final Reply reply = this.clientImpl.getClientChainReplicationFacade()
							.readResponsesForRequest(request.getRequest());
					retry++;
					if (reply != null)
						this.clientImpl.logMessage("Reply from Server for request - "
								+ request.toString() + ":" + reply.toString());
					else {
						this.clientImpl.logMessage("Did not get a reply for request - "
								+ request.toString());
						if (retry <= retryCount) {
							this.clientImpl.logMessage("Retrying request again (retry count left:"
									+ (retryCount - retry)
									+ ") - "
									+ request.toString());
						}
					}

				}
			}
		}
	}


	/**
	 * Gets the request dispatcher.
	 *
	 * @return the request dispatcher
	 */
	public RequestDispatcher getRequestDispatcher() {
		return this.requestDispatcher;
	}
}
