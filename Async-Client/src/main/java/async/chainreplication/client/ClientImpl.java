package async.chainreplication.client;

import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.client.threads.ResponseListener;
import async.common.util.Config;
import async.common.util.ConfigUtil;


public class ClientImpl {
	MasterUpdateListenerThread masterUpdateListener;
	ResponseListener responseListener;
	ClientChainReplicationFacade clientChainReplicationFacade;

	public static void main(String args[]) {
		ClientImpl clientImpl = new ClientImpl(
				ConfigUtil.convertToConfig(args[0]),
				args[1]);
		clientImpl.initAndStartClient();
		clientImpl.performOperations();
		clientImpl.stopClient();
	}

	public ClientImpl(Config config, String clientId) {
		clientChainReplicationFacade = new ClientChainReplicationFacade(
				config.getClients().get(clientId), config.getChains(),
				config.getMaster());
	}

	public ClientChainReplicationFacade getClientChainReplicationFacade() {
		return clientChainReplicationFacade;
	}

	private void performOperations() {

	}

	private void initAndStartClient() {
		masterUpdateListener = new MasterUpdateListenerThread(this);
		masterUpdateListener.start();
		responseListener = new ResponseListener(this);
		responseListener.start();
	}

	private void stopClient() {
		masterUpdateListener.stopThread();
		responseListener.stopThread();
	}

}
