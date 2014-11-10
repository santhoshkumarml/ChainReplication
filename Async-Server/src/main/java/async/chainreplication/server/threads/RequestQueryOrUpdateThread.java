package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPServerStarterHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestQueryOrUpdateThread.
 */
public class RequestQueryOrUpdateThread extends Thread {

	/** The request server helper. */
	IServerStarterHelper requestServerHelper;
	
	/** The server impl. */
	ServerImpl serverImpl;
	
	/** The should still run. */
	boolean shouldStillRun = true;

	/**
	 * Instantiates a new request query or update thread.
	 *
	 * @param serverImpl the server impl
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public RequestQueryOrUpdateThread(ServerImpl serverImpl)
			throws ServerChainReplicationException {
		this.serverImpl = serverImpl;
		if (this.serverImpl.isHeadInTheChain()
				|| this.serverImpl.isTailInTheChain()) {
			requestServerHelper = new UDPServerStarterHelper(this.serverImpl
					.getServer().getServerProcessDetails().getUdpPort());
			try {
				requestServerHelper.initAndStartServer();
			} catch (ConnectServerException e) {
				throw new ServerChainReplicationException(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (shouldStillRun
				&& (serverImpl.isHeadInTheChain() || serverImpl
						.isTailInTheChain())) {
			ChainReplicationMessage message = null;
			try {
				message = (ChainReplicationMessage) requestServerHelper
						.acceptAndReadObjectConnection();
			} catch (ConnectServerException e) {
				e.printStackTrace();
				serverImpl.logMessage("Internal Error:" + e.getMessage());
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
		requestServerHelper.stopServer();
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		shouldStillRun = false;
		requestServerHelper.stopServer();
	}
}
