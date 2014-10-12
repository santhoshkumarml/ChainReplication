package async.chainreplication.server.threads;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPServerStarterHelper;

public class RequestQueryOrUpdateThread extends Thread{

	IServerStarterHelper requestServerHelper;
	ServerImpl serverImpl;
	
	
	public RequestQueryOrUpdateThread(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		if(this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isHeadInTheChain()) {
			this.requestServerHelper = new UDPServerStarterHelper();
		}
	}
	public void run() {
		while(this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isHeadInTheChain()) {
             ChainReplicationMessage message = 
            		 (ChainReplicationMessage)this.requestServerHelper.acceptAndReadObjectConnection();
             this.serverImpl.deliverMessage(message);
		}
	}
}
