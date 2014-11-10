package async.chainreplication.master.threads;

import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplocation.master.MasterImpl;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class HeartBeatListenerThread.
 */
public class HeartBeatListenerThread extends Thread {
	
	/** The should still run. */
	volatile boolean shouldStillRun = true;
	
	/** The heart beat server helper. */
	IServerStarterHelper heartBeatServerHelper;
	
	/** The master impl. */
	MasterImpl masterImpl;

	/**
	 * Instantiates a new heart beat listener thread.
	 *
	 * @param masterImpl the master impl
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public HeartBeatListenerThread(MasterImpl masterImpl)
			throws MasterChainReplicationException {
		this.masterImpl = masterImpl;
		heartBeatServerHelper = new TCPServerStarterHelper(masterImpl
				.getMaster().getMasterPort());
		try {
			heartBeatServerHelper.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new MasterChainReplicationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (shouldStillRun) {
			try {
				HeartBeatMessage heartBeatMessage = (HeartBeatMessage) heartBeatServerHelper
						.acceptAndReadObjectConnection();
				masterImpl.getMasterChainReplicationFacade().deliverMessage(
						heartBeatMessage);
			} catch (ConnectServerException e) {
				heartBeatServerHelper.stopServer();
				masterImpl.logMessage("Internal Error:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		shouldStillRun = false;
	}
}
