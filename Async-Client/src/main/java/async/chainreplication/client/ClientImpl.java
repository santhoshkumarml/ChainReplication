package async.chainreplication.client;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.client.threads.ResponseListener;
import async.chainreplication.master.models.Client;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class ClientImpl extends ChainReplicationImpl{
	MasterUpdateListenerThread masterUpdateListener;
	ResponseListener responseListener;
	ClientChainReplicationFacade clientChainReplicationFacade;

	public static void main(String args[]) {
		ClientImpl clientImpl = new ClientImpl(
				ConfigUtil.convertToConfig(args[0]),
				args[1]);
		clientImpl.init();
		clientImpl.performOperations();
		clientImpl.stop();
	}

	public ClientImpl(Config config, String clientId) {
		super(clientId);
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


	private void performOperations() {

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
