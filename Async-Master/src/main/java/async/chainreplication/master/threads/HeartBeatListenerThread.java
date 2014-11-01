package async.chainreplication.master.threads;

import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplocation.master.MasterImpl;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;

public class HeartBeatListenerThread extends Thread {
	volatile boolean shouldStillRun = true;
	IServerStarterHelper heartBeatServerHelper;
	MasterImpl masterImpl;

	public HeartBeatListenerThread(MasterImpl masterImpl) throws MasterChainReplicationException {
		this.masterImpl = masterImpl;
		this.heartBeatServerHelper = new TCPServerStarterHelper(
				masterImpl.getMaster().getMasterPort());
		try {
			this.heartBeatServerHelper.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new MasterChainReplicationException(e);
		}
	}

	public void run() {
		while(shouldStillRun) {
			try {
				HeartBeatMessage heartBeatMessage = (HeartBeatMessage) this.heartBeatServerHelper.acceptAndReadObjectConnection();
				this.masterImpl.getMasterChainReplicationFacade().deliverMessage(heartBeatMessage);
			} catch (ConnectServerException e) {
				this.heartBeatServerHelper.stopServer();
				this.masterImpl.logMessage("Internal Error:"+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void stopThread() {
		shouldStillRun = false;
	}
}
