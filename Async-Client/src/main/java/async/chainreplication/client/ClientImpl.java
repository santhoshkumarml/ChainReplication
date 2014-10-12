package async.chainreplication.client;

import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.client.threads.ResponseListener;
import async.chainreplication.communication.messages.ChainReplicationMessage;


public class ClientImpl {
	MasterUpdateListenerThread masterUpdateListener;
	ResponseListener responseListener;
	ClientChainReplicationFacade clientChainReplicationFacade;

	public static void main(String args[]) {
		String clientId = args[0];
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		String masterHost = args[3];
		int masterPort = Integer.parseInt(args[4]);
		ClientImpl clientImpl = new ClientImpl(
				clientId, host, port,
				masterHost, masterPort);
		clientImpl.initAndStartClient();
		clientImpl.performOperations();
		clientImpl.stopClient();
	}

	public ClientImpl(String clientId, String host, int port,
			String masterHost, int masterPort) {
		clientChainReplicationFacade = new ClientChainReplicationFacade();
	}

	public void deliverMessage(ChainReplicationMessage message) {
		this.clientChainReplicationFacade.deliverMessage(message);

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
