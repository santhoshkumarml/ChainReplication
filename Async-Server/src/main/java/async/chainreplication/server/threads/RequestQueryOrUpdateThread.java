package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.ConnectServerException;
import async.connection.util.UDPServerStarterHelper;

public class RequestQueryOrUpdateThread extends Thread{

	IServerStarterHelper requestServerHelper;
	ServerImpl serverImpl;
	boolean shouldStillRun = true;


	public RequestQueryOrUpdateThread(ServerImpl serverImpl) throws ServerChainReplicationException {
		this.serverImpl = serverImpl;
		if(this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isTailInTheChain()) {
			this.requestServerHelper = new UDPServerStarterHelper(
					this.serverImpl.getServer().getServerProcessDetails(
							).getUdpPort());
			try {
				this.requestServerHelper.initAndStartServer();
			} catch (ConnectServerException e) {
				throw new ServerChainReplicationException(e);
			}
		}
	}
	public void run() {
		while(shouldStillRun && (this.serverImpl.isHeadInTheChain()
				||this.serverImpl.isTailInTheChain())) {
			ChainReplicationMessage message = null;
			try {
				message = (ChainReplicationMessage)this.requestServerHelper.acceptAndReadObjectConnection();
			} catch (ConnectServerException e) {
				e.printStackTrace();
				this.serverImpl.logMessage("Internal Error:"+e.getMessage());
				break;
			}
			this.serverImpl.getServerChainReplicationFacade().deliverMessage(message);
			this.serverImpl.getServerChainReplicationFacade().getServerMessageHandler().incrementReceiveSequenceNumber();
			int receiveSequenceNumber = this.serverImpl.getServerChainReplicationFacade().getServerMessageHandler().getReceiveSequenceNumber();
			this.serverImpl.logMessage("Incoming Message-"+receiveSequenceNumber+":"+message.toString());
		}
		this.requestServerHelper.stopServer();
	}

	public void stopThread() {
		shouldStillRun = false;
		requestServerHelper.stopServer();
	}
}
