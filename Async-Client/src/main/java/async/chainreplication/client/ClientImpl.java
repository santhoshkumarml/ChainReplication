package async.chainreplication.client;

import async.chainreplication.client.exception.ClientChainReplicationException;
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
	long responseWaitTime = 3000;
	MasterUpdateListenerThread masterUpdateListener;
	ResponseListener responseListener;
	ClientChainReplicationFacade clientChainReplicationFacade;
	String clientId;

	public static void main(String args[]) {
		Config config = ConfigUtil.convertToConfig(args[0]);
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
			this.clientChainReplicationFacade.handleMessage(clientRequestMessage);
			try {
				Thread.sleep(responseWaitTime);
			} catch(InterruptedException e) {
				throw new ClientChainReplicationException(e);
			}
		}
	}

	public void init() {
		super.init();
		masterUpdateListener = new MasterUpdateListenerThread(this);
		masterUpdateListener.start();
		responseListener = new ResponseListener(this);
		responseListener.start();
	}

	public void stop() {
		masterUpdateListener.stopThread();
		responseListener.stopThread();
		super.stop();
	}

	public void logMessage(String message) {
		this.getLogMessages().enqueueMessage(message);
	}
}
