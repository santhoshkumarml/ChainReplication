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
		try {
			clientImpl.performOperations(config);
		} catch (final ClientChainReplicationException e) {
			e.printStackTrace();
		}
		clientImpl.stop();
	}

	/** The response wait time. */
	long responseWaitTime = 10000;

	/** The master update listener. */
	MasterUpdateListenerThread masterUpdateListener;

	/** The response listener. */
	ResponseListener responseListener;

	/** The client chain replication facade. */
	ClientChainReplicationFacade clientChainReplicationFacade;

	/** The client id. */
	String clientId;

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
		// masterUpdateListener = new MasterUpdateListenerThread(this);
		// masterUpdateListener.start();
		try {
			responseListener = new ResponseListener(this);
		} catch (final ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
			this.stop();
		}
		responseListener.start();
		this.logMessage("Client Started");
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

	/**
	 * Perform operations.
	 *
	 * @param config
	 *            the config
	 * @throws ClientChainReplicationException
	 *             the client chain replication exception
	 */
	private void performOperations(Config config)
			throws ClientChainReplicationException {
		final TestCases testcases = config.getTestCases().get(
				config.getClients().get(clientId));
		for (final RequestWithChain request : testcases.getRequests()) {
			final int retryCount = request.getRequest().getRetryCount();
			int retry = 0;
			while (retry <= retryCount) {
				final RequestMessage requestMessage = new RequestMessage(
						request.getRequest());
				final ClientRequestMessage clientRequestMessage = new ClientRequestMessage(
						request.getChainName(), requestMessage);
				clientChainReplicationFacade
				.deliverMessage(clientRequestMessage);
				try {
					Thread.sleep(responseWaitTime);
				} catch (final InterruptedException e) {
					throw new ClientChainReplicationException(e);
				}
				final Reply reply = clientChainReplicationFacade
						.readResponsesForRequest(request.getRequest());
				retry++;
				if (reply != null)
					this.logMessage("Reply from Server for request - "
							+ request.toString() + ":" + reply.toString());
				else {
					this.logMessage("Did not get a reply for request - "
							+ request.toString());
					if (retry <= retryCount) {
						this.logMessage("Retrying request again (retry count left:"
								+ (retryCount - retry)
								+ ") - "
								+ request.toString());
					}
				}

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#stop()
	 */
	public void stop() {
		this.logMessage("Client Stopping");
		responseListener.stopThread();
		super.stop();
	}
}
