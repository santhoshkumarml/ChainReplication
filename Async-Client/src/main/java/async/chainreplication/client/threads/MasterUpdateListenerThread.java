package async.chainreplication.client.threads;

import async.chainreplication.client.ClientImpl;
import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;


public class MasterUpdateListenerThread extends Thread{
	ClientImpl clientImpl;
	volatile boolean shouldStillRun = true;
	IServerStarterHelper masterMessageListener = null;

	public MasterUpdateListenerThread(ClientImpl clientImpl) throws ClientChainReplicationException {
		this.clientImpl = clientImpl;
		this.masterMessageListener = new TCPServerStarterHelper(
				this.clientImpl.getClient().getClientProcessDetails().getTcpPort());
		try {
			this.masterMessageListener.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new ClientChainReplicationException(e);
		}
	}
	public void run() {
		while(shouldStillRun) {
			ChainReplicationMessage masterMessage = null;
			try {
				masterMessage = (ChainReplicationMessage)this.masterMessageListener.acceptAndReadObjectConnection();
				this.clientImpl.getClientChainReplicationFacade().deliverMessage(
						masterMessage);
			} catch (ConnectServerException | ClientChainReplicationException e) {
				this.clientImpl.logMessage("Internal Error:"+e.getMessage());
				this.stopThread();
				e.printStackTrace();
				break;
			}
			this.clientImpl.getClientChainReplicationFacade().getClientMessageHandler().incrementReceiveSequenceNumber();
			int receiveSequenceNumber = this.clientImpl.getClientChainReplicationFacade().getClientMessageHandler().getReceiveSequenceNumber();
			this.clientImpl.logMessage("Incoming Message-"+receiveSequenceNumber+":"+masterMessage.toString());
		}
	}
	
	public void stopThread() {
		shouldStillRun = false;
		this.masterMessageListener.stopServer();
	}

	
}
