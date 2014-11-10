package async.chainreplication.client.threads;

import async.chainreplication.client.ClientImpl;
import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.connection.util.ConnectServerException;
import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPServerStarterHelper;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving response events.
 * The class that is interested in processing a response
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addResponseListener<code> method. When
 * the response event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ResponseEvent
 */
public class ResponseListener extends Thread {
	
	/** The response server helper. */
	IServerStarterHelper responseServerHelper;
	
	/** The client impl. */
	ClientImpl clientImpl;
	
	/** The should still run. */
	volatile boolean shouldStillRun = true;

	/**
	 * Instantiates a new response listener.
	 *
	 * @param clientImpl the client impl
	 * @throws ClientChainReplicationException the client chain replication exception
	 */
	public ResponseListener(ClientImpl clientImpl)
			throws ClientChainReplicationException {
		this.clientImpl = clientImpl;
		responseServerHelper = new UDPServerStarterHelper(this.clientImpl
				.getClient().getClientProcessDetails().getUdpPort());
		try {
			responseServerHelper.initAndStartServer();
		} catch (ConnectServerException e) {
			throw new ClientChainReplicationException(e);
		}
		this.clientImpl = clientImpl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (shouldStillRun) {
			ChainReplicationMessage responseMessage = null;
			try {
				responseMessage = (ChainReplicationMessage) responseServerHelper
						.acceptAndReadObjectConnection();
				clientImpl.getClientChainReplicationFacade().deliverMessage(
						responseMessage);
			} catch (ClientChainReplicationException | ConnectServerException e) {
				shouldStillRun = false;
				e.printStackTrace();
				break;
			}
			clientImpl.getClientChainReplicationFacade()
					.getClientMessageHandler().incrementReceiveSequenceNumber();
			int receiveSequenceNumber = clientImpl
					.getClientChainReplicationFacade()
					.getClientMessageHandler().getReceiveSequenceNumber();
			clientImpl.logMessage("Incoming Message-" + receiveSequenceNumber
					+ ":" + responseMessage.toString());
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		shouldStillRun = false;
		responseServerHelper.stopServer();
	}

}
