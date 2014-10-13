package async.chainreplication.client.threads;

import async.chainreplication.client.ClientImpl;
import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.connection.util.IServerStarterHelper;
import async.connection.util.ConnectServerException;
import async.connection.util.UDPServerStarterHelper;

public class ResponseListener extends Thread{
	IServerStarterHelper responseServerHelper;
	ClientImpl clientImpl;
	volatile boolean shouldStillRun = true;

	public ResponseListener(ClientImpl clientImpl) throws ClientChainReplicationException {
		this.clientImpl = clientImpl;
		this.responseServerHelper = new UDPServerStarterHelper(
				this.clientImpl.getClient().getClientProcessDetails().getUdpPort());
		try {
			this.responseServerHelper.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new ClientChainReplicationException(e);
		}
		this.clientImpl = clientImpl;
	}
	public void run() {
		while(shouldStillRun) {
			ChainReplicationMessage responseMessage = null;
			try {
				responseMessage = (ChainReplicationMessage)this.responseServerHelper.acceptAndReadObjectConnection();
				clientImpl.getClientChainReplicationFacade().deliverMessage(
						responseMessage);
			} catch (ClientChainReplicationException | ConnectServerException e) {
				shouldStillRun = false;
				e.printStackTrace();
				break;
			}	
		}
	}
	
	public void stopThread() {
		shouldStillRun = false;
		responseServerHelper.stopServer();
		this.stop();
	}

}
