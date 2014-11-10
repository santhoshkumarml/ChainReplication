package async.chainreplication.client;

import java.util.Map;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterClientChangeMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.generic.message.queue.MessageQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientChainReplicationFacade.
 */
public class ClientChainReplicationFacade {
	
	/** The messages. */
	MessageQueue<ChainReplicationMessage> messages = new MessageQueue<ChainReplicationMessage>();
	
	/** The client message handler. */
	ClientMessageHandler clientMessageHandler;
	
	/** The client impl. */
	ClientImpl clientImpl;

	/**
	 * Instantiates a new client chain replication facade.
	 *
	 * @param client the client
	 * @param chainNameToChainMap the chain name to chain map
	 * @param master the master
	 * @param clientImpl the client impl
	 * @throws ClientChainReplicationException the client chain replication exception
	 */
	public ClientChainReplicationFacade(Client client,
			Map<String, Chain> chainNameToChainMap, Master master,
			ClientImpl clientImpl) throws ClientChainReplicationException {
		clientMessageHandler = new ClientMessageHandler(client,
				chainNameToChainMap, master, this);
		this.clientImpl = clientImpl;
	}

	/**
	 * Deliver message.
	 *
	 * @param message the message
	 * @throws ClientChainReplicationException the client chain replication exception
	 */
	public void deliverMessage(ChainReplicationMessage message)
			throws ClientChainReplicationException {
		if (message != null) {
			messages.enqueueMessageObject(message.getPriority().ordinal(),
					message);
		}
		while (messages.hasMoreMessages()) {
			ChainReplicationMessage oldMessage = (ChainReplicationMessage) messages
					.dequeueMessageAndReturnMessageObject();
			handleMessage(oldMessage);
		}
	}

	/**
	 * Gets the client message handler.
	 *
	 * @return the client message handler
	 */
	public ClientMessageHandler getClientMessageHandler() {
		return clientMessageHandler;
	}

	/**
	 * Handle message.
	 *
	 * @param message the message
	 * @throws ClientChainReplicationException the client chain replication exception
	 */
	public void handleMessage(ChainReplicationMessage message)
			throws ClientChainReplicationException {
		this.logMessage(message.toString());
		if (message.getClass() == ClientRequestMessage.class) {
			clientMessageHandler
					.handleClientRequestMessage((ClientRequestMessage) message);
		} else if (message.getClass() == ResponseOrSyncMessage.class) {
			clientMessageHandler
					.handleReponseMessage((ResponseOrSyncMessage) message);
		} else if (message instanceof MasterClientChangeMessage) {
			clientMessageHandler
					.handleMasterMessage((MasterClientChangeMessage) message);
		}
	}

	/**
	 * Log message.
	 *
	 * @param message the message
	 */
	public void logMessage(String message) {
		clientImpl.logMessage(message);
	}

	/**
	 * Read responses for request.
	 *
	 * @param request the request
	 * @return the reply
	 */
	public Reply readResponsesForRequest(Request request) {
		return clientMessageHandler.readResponses(request);
	}

}
