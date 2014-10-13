package async.chainreplication.client;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.client.threads.ResponseListener;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.master.models.Client;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import async.common.util.TestCases;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class ClientImpl extends ChainReplicationImpl{
	long responseWaitTime = 4000;
	MasterUpdateListenerThread masterUpdateListener;
	ResponseListener responseListener;
	ClientChainReplicationFacade clientChainReplicationFacade;
	String clientId;

	public static void main(String args[]) {
		Config config = ConfigUtil.deserializeFromFile(args[0]);
		ClientImpl clientImpl = new ClientImpl(
				config,
				args[1]);
		clientImpl.init();
		try {
			clientImpl.performOperations(config);
		} catch (ClientChainReplicationException e) {
			e.printStackTrace();
		}
		clientImpl.stop();
	}

	public ClientImpl(Config config, String clientId) {
		super(clientId);
		this.clientId = clientId;
		try {
			clientChainReplicationFacade = new ClientChainReplicationFacade(
					config.getClients().get(clientId), config.getChains(),
					config.getMaster(), this);
		} catch (ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
		}
		this.logMessage("Client Started:"+config.getClients().get(clientId));
	}

	public ClientChainReplicationFacade getClientChainReplicationFacade() {
		return clientChainReplicationFacade;
	}

	public Client getClient() {
		return this.clientChainReplicationFacade.getClientMessageHandler().getClient();
	}


	private void performOperations(Config config) throws ClientChainReplicationException {
		TestCases testcases = config.getTestCases().get(config.getClients().get(clientId));
		for(Request request : testcases.getRequests()) {
			RequestMessage requestMessage = new RequestMessage(request);
			ClientRequestMessage clientRequestMessage = new ClientRequestMessage(testcases.getChainName(), requestMessage);
			this.clientChainReplicationFacade.deliverMessage(clientRequestMessage);
			try {
				Thread.sleep(responseWaitTime);
			} catch(InterruptedException e) {
				throw new ClientChainReplicationException(e);
			}
			Reply reply = this.clientChainReplicationFacade.readResponsesForRequest(request);
			if(reply != null)
				this.logMessage("Reply from Server for request - "+request.toString()+":"+reply.toString());
			else
				this.logMessage("Did not get a reply for request - "+ request.toString());
		}
	}

	public void init() {
		super.init();
		//masterUpdateListener = new MasterUpdateListenerThread(this);
		//masterUpdateListener.start();
		try {
			responseListener = new ResponseListener(this);
		} catch (ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
			this.stop();
		}
		responseListener.start();
		this.logMessage("Client Started"+this.clientId);
	}

	public void stop() {
		responseListener.stopThread();
		super.stop();
	}

	public void logMessage(String message) {
		this.getLogMessages().enqueueMessage(message);
	}
}
