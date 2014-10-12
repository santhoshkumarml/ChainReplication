package async.chainreplication.client.thread;

import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.master.models.Chain;


public class ClientImpl {
	
	Map<String, Chain> chainNameToChainMap = new HashMap<String, Chain>();
	MasterUpdateListenerThread masterUpdateListenerThread;

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
	}
	
	public ClientImpl(String clientId, String host, int port,
			String masterHost, int masterPort) {

	}

	private void performOperations() {
		
	}

	private void initAndStartClient() {
		masterUpdateListenerThread = new MasterUpdateListenerThread();
		masterUpdateListenerThread.start();
		
	}



}
