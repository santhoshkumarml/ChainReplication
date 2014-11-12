package async.chainreplication.master.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplocation.master.MasterImpl;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class HeartBeatListenerThread.
 */
public class ServerMessageListenerThread extends Thread {
	
	/** The should still run. */
	volatile boolean shouldStillRun = true;
	
	/** The heart beat server helper. */
	IServerStarterHelper serverMessagesHelper;
	
	/** The master impl. */
	MasterImpl masterImpl;

	/**
	 * Instantiates a new heart beat listener thread.
	 *
	 * @param masterImpl the master impl
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public ServerMessageListenerThread(MasterImpl masterImpl)
			throws MasterChainReplicationException {
		this.masterImpl = masterImpl;
		serverMessagesHelper = new TCPServerStarterHelper(masterImpl
				.getMaster().getMasterPort());
		try {
			serverMessagesHelper.initAndStartServer();
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
				ChainReplicationMessage message = (ChainReplicationMessage) serverMessagesHelper
						.acceptAndReadObjectConnection();
				masterImpl.getMasterChainReplicationFacade().deliverMessage(message);
			} catch (ConnectServerException e) {
				serverMessagesHelper.stopServer();
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
