package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.IServerStarterHelper;
import async.connection.util.ConnectServerException;
import async.connection.util.UDPServerStarterHelper;

public class RequestQueryOrUpdateThread extends Thread{

	IServerStarterHelper requestServerHelper;
	ServerImpl serverImpl;
	boolean shouldStillRun = true;


	public RequestQueryOrUpdateThread(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		if(this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isHeadInTheChain()) {
			this.requestServerHelper = new UDPServerStarterHelper(
					this.serverImpl.getServer().getServerProcessDetails(
							).getUdpPort());
		}
	}
	public void run() {
		while(shouldStillRun && (this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isHeadInTheChain())) {
			ChainReplicationMessage message = null;
			try {
				message = (ChainReplicationMessage)this.requestServerHelper.acceptAndReadObjectConnection();
			} catch (ConnectServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.serverImpl.getServerChainReplicationFacade().deliverMessage(message);
		}
		this.requestServerHelper.stopServer();
	}

	public void stopThread() {
		shouldStillRun = false;
	}
}
