package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPServerStarterHelper;

public class RequestQueryOrUpdateThread extends Thread{

	IServerStarterHelper requestServerHelper;
	ServerImpl serverImpl;
	boolean shouldStillRun = true;
	
	
	public RequestQueryOrUpdateThread(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		if(this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isHeadInTheChain()) {
			this.requestServerHelper = new UDPServerStarterHelper();
		}
	}
	public void run() {
		while(shouldStillRun && (this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isHeadInTheChain())) {
             ChainReplicationMessage message = 
            		 (ChainReplicationMessage)this.requestServerHelper.acceptAndReadObjectConnection();
             this.serverImpl.deliverMessage(message);
		}
		this.requestServerHelper.stopServer();
	}
	
	public void stopThread() {
		shouldStillRun = false;
	}
}
