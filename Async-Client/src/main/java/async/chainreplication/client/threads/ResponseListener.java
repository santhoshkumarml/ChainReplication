package async.chainreplication.client.threads;

import async.chainreplication.client.ClientImpl;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPServerStarterHelper;

public class ResponseListener extends Thread{
	IServerStarterHelper responseServerHelper;
	ClientImpl clientImpl;
	boolean shouldStillRun = true;

	public ResponseListener(ClientImpl clientImpl) {
		this.responseServerHelper = new UDPServerStarterHelper();
		this.clientImpl = clientImpl;
	}
	public void run() {
		while(shouldStillRun) {
			ChainReplicationMessage responseMessage = 
					(ChainReplicationMessage)this.responseServerHelper.acceptAndReadObjectConnection();
			clientImpl.deliverMessage(responseMessage);
			
		}
	}
	
	public void stopThread() {
		shouldStillRun = false;
	}

}
