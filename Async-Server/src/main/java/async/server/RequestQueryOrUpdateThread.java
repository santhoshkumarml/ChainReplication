package async.server;

import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPServerStarterHelper;

public class RequestQueryOrUpdateThread extends Thread{

	IServerStarterHelper requestServerHelper;
	ServerImpl serverImpl;
	
	
	public RequestQueryOrUpdateThread(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		if(this.serverImpl.getChainReplicationFacade().isHeadInTheChain()
				||this.serverImpl.getChainReplicationFacade().isHeadInTheChain()) {
			this.requestServerHelper = new UDPServerStarterHelper();
		}
	}
	public void run() {
		while(true) {
			//this.requestServerHelper.
		   
		}
	}
}
