package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainMessageListenerThread.
 */
public class ChainMessageListenerThread extends Thread {

	/** The chain messages listener. */
	IServerStarterHelper chainMessagesListener;
	
	/** The server impl. */
	ServerImpl serverImpl;
	
	/** The should still run. */
	boolean shouldStillRun = true;

	/**
	 * Instantiates a new chain message listener thread.
	 *
	 * @param serverImpl the server impl
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public ChainMessageListenerThread(ServerImpl serverImpl)
			throws ServerChainReplicationException {
		this.serverImpl = serverImpl;
		chainMessagesListener = new TCPServerStarterHelper(this.serverImpl
				.getServer().getServerProcessDetails().getTcpPort());
		try {
			chainMessagesListener.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new ServerChainReplicationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (shouldStillRun) {
			ChainReplicationMessage message = null;
			try {
				message = (ChainReplicationMessage) chainMessagesListener
						.acceptAndReadObjectConnection();
			} catch (ConnectServerException e) {
				serverImpl.logMessage("Internal Error:" + e.getMessage());
				this.stopThread();
				e.printStackTrace();
				break;
			}
			serverImpl.getServerChainReplicationFacade()
					.deliverMessage(message);
			/*serverImpl.getServerChainReplicationFacade()
					.getServerMessageHandler().incrementReceiveSequenceNumber();
			int receiveSequenceNumber = serverImpl
					.getServerChainReplicationFacade()
					.getServerMessageHandler().getReceiveSequenceNumber();
			serverImpl.logMessage("Incoming Message-" + receiveSequenceNumber
					+ ":" + message.toString());*/
		}

	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		shouldStillRun = false;
		chainMessagesListener.stopServer();
	}
}
