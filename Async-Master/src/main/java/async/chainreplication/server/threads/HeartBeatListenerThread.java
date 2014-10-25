package async.chainreplication.server.threads;

import async.chainreplication.master.exception.MasterChainReplicationException;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;
import async.master.MasterImpl;

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


		}
	}

	public void stopThread() {
		shouldStillRun = false;
	}
}