package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.ConnectServerException;
import async.connection.util.TCPServerStarterHelper;

public class ChainMessageListenerThread extends Thread{

	IServerStarterHelper chainMessagesListener;
	ServerImpl serverImpl;
	boolean shouldStillRun = true;

	public ChainMessageListenerThread(ServerImpl serverImpl) throws ServerChainReplicationException {
		this.serverImpl = serverImpl;
		this.chainMessagesListener = new TCPServerStarterHelper(
				this.serverImpl.getServer().getServerProcessDetails().getTcpPort());
		try {
			this.chainMessagesListener.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new ServerChainReplicationException(e);
		}
	}

	public void run()  {
		while(shouldStillRun) {
			ChainReplicationMessage message = null;
			try {
				message = (ChainReplicationMessage)this.chainMessagesListener.acceptAndReadObjectConnection();
			} catch (ConnectServerException e) {
				this.serverImpl.logMessage("Internal Error:"+e.getMessage());
				e.printStackTrace();
			}
			this.serverImpl.getServerChainReplicationFacade().deliverMessage(message);
			this.serverImpl.getServerChainReplicationFacade().getServerMessageHandler().incrementReceiveSequenceNumber();
			int receiveSequenceNumber = this.serverImpl.getServerChainReplicationFacade().getServerMessageHandler().getReceiveSequenceNumber();
			this.serverImpl.logMessage("Incoming Message-"+receiveSequenceNumber+":"+message.toString());
		}
		this.chainMessagesListener.stopServer();
	}

	public void stopThread() {
		shouldStillRun = false;
		chainMessagesListener.stopServer();
	}
}
