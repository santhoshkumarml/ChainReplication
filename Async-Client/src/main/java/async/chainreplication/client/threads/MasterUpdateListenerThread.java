package async.chainreplication.client.threads;

import async.chainreplication.client.ClientImpl;
import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterUpdateListenerThread.
 */
public class MasterUpdateListenerThread extends Thread {
	
	/** The client impl. */
	ClientImpl clientImpl;
	
	/** The should still run. */
	volatile boolean shouldStillRun = true;
	
	/** The master message listener. */
	IServerStarterHelper masterMessageListener = null;

	/**
	 * Instantiates a new master update listener thread.
	 *
	 * @param clientImpl the client impl
	 * @throws ClientChainReplicationException the client chain replication exception
	 */
	public MasterUpdateListenerThread(ClientImpl clientImpl)
			throws ClientChainReplicationException {
		this.clientImpl = clientImpl;
		masterMessageListener = new TCPServerStarterHelper(this.clientImpl
				.getClient().getClientProcessDetails().getTcpPort());
		try {
			masterMessageListener.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new ClientChainReplicationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (shouldStillRun) {
			ChainReplicationMessage masterMessage = null;
			try {
				masterMessage = (ChainReplicationMessage) masterMessageListener
						.acceptAndReadObjectConnection();
				clientImpl.getClientChainReplicationFacade().deliverMessage(
						masterMessage);
			} catch (ConnectServerException | ClientChainReplicationException e) {
				clientImpl.logMessage("Internal Error:" + e.getMessage());
				this.stopThread();
				e.printStackTrace();
				break;
			}
			clientImpl.getClientChainReplicationFacade()
					.getClientMessageHandler().incrementReceiveSequenceNumber();
			int receiveSequenceNumber = clientImpl
					.getClientChainReplicationFacade()
					.getClientMessageHandler().getReceiveSequenceNumber();
			clientImpl.logMessage("Incoming Message-" + receiveSequenceNumber
					+ ":" + masterMessage.toString());
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		shouldStillRun = false;
		masterMessageListener.stopServer();
	}

}
