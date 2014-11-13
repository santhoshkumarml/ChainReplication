package async.chainreplication.client;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.MasterClientChangeMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.connection.util.ConnectClientException;
import async.connection.util.IClientHelper;
import async.connection.util.UDPClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientMessageHandler.
 */
public class ClientMessageHandler {

	/** The client. */
	Client client;

	/** The chain name to chain map. */
	Map<String, Chain> chainNameToChainMap = new HashMap<String, Chain>();

	public Map<String, Chain> getChainNameToChainMap() {
		return chainNameToChainMap;
	}

	/** The master. */
	Master master;

	/** The client message client helper. */
	IClientHelper clientMessageClientHelper;

	/** The application reply handler. */
	IApplicationReplyHandler applicationReplyHandler;

	/** The client chain replication facade. */
	ClientChainReplicationFacade clientChainReplicationFacade;

	/** The send sequence number. */
	volatile int sendSequenceNumber = 0;

	/** The receive sequence number. */
	volatile int receiveSequenceNumber = 0;

	/**
	 * Instantiates a new client message handler.
	 *
	 * @param client
	 *            the client
	 * @param chainNameToChainMap
	 *            the chain name to chain map
	 * @param master
	 *            the master
	 * @param clientChainReplicationFacade
	 *            the client chain replication facade
	 * @throws ClientChainReplicationException
	 *             the client chain replication exception
	 */
	public ClientMessageHandler(Client client,
			Map<String, Chain> chainNameToChainMap, Master master,
			ClientChainReplicationFacade clientChainReplicationFacade)
					throws ClientChainReplicationException {
		this.client = client;
		this.chainNameToChainMap.putAll(chainNameToChainMap);
		this.master = master;
		this.clientChainReplicationFacade = clientChainReplicationFacade;

		try {
			applicationReplyHandler = (IApplicationReplyHandler) Class
					.forName(
							"async.chainreplication.app.client."
									+ "handler.ApplicationReplyHandler")
									.getConstructor(ClientMessageHandler.class)
									.newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new ClientChainReplicationException(e);
		}
	}

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Gets the client chain replication facade.
	 *
	 * @return the client chain replication facade
	 */
	public ClientChainReplicationFacade getClientChainReplicationFacade() {
		return clientChainReplicationFacade;
	}

	/**
	 * Gets the head for chain.
	 *
	 * @param chainName
	 *            the chain name
	 * @return the head for chain
	 */
	private Server getHeadForChain(String chainName) {
		synchronized (chainNameToChainMap) {
			return chainNameToChainMap.get(chainName).getHead();
		}
	}

	/**
	 * Gets the master.
	 *
	 * @return the master
	 */
	public Master getMaster() {
		return master;
	}

	/**
	 * Gets the receive sequence number.
	 *
	 * @return the receive sequence number
	 */
	public int getReceiveSequenceNumber() {
		return receiveSequenceNumber;
	}

	/**
	 * Gets the send sequence number.
	 *
	 * @return the send sequence number
	 */
	public int getSendSequenceNumber() {
		return sendSequenceNumber;
	}

	/**
	 * Gets the tail for chain.
	 *
	 * @param chainName
	 *            the chain name
	 * @return the tail for chain
	 */
	private Server getTailForChain(String chainName) {
		synchronized (chainNameToChainMap) {
			return chainNameToChainMap.get(chainName).getTail();
		}
	}

	// ---------------------------------------------------------------------------------
	// Client Handler Methods
	/**
	 * Handle client request message.
	 *
	 * @param message
	 *            the message
	 * @throws ClientChainReplicationException
	 *             the client chain replication exception
	 */
	public void handleClientRequestMessage(ClientRequestMessage message)
			throws ClientChainReplicationException {
		final String chainName = message.getChainName();
		switch (message.getRequestMessage().getRequest().getRequestType()) {
		case QUERY:
			final Server tail = getTailForChain(chainName);
			clientMessageClientHelper = new UDPClientHelper(tail
					.getServerProcessDetails().getHost(), tail
					.getServerProcessDetails().getUdpPort());
			break;
		case UPDATE:
			final Server head = getHeadForChain(chainName);
			clientMessageClientHelper = new UDPClientHelper(head
					.getServerProcessDetails().getHost(), head
					.getServerProcessDetails().getUdpPort());
			break;
		}
		try {
			clientMessageClientHelper.sendMessage(message.getRequestMessage());
		} catch (final ConnectClientException e) {
			throw new ClientChainReplicationException(e.getMessage()+"--port"+clientMessageClientHelper.getServerPort());
		}
		/*
		 * incrementSendSequenceNumber();
		 * this.getClientChainReplicationFacade().logMessage(
		 * "Outgoing Message-" + sendSequenceNumber + ":" + message.toString());
		 */
	}

	/**
	 * Handle master message.
	 *
	 * @param message
	 *            the message
	 */
	public void handleMasterMessage(MasterClientChangeMessage message) {
		final Set<Chain> chains = message.getChainChanges();
		if (!chains.isEmpty()) {
			synchronized (chainNameToChainMap) {
				for (final Chain chain : chains) {
					chainNameToChainMap.put(chain.getChainName(), chain);
				}
			}
		}

	}

	/**
	 * Handle reponse message.
	 *
	 * @param message
	 *            the message
	 */
	public void handleReponseMessage(ResponseOrSyncMessage message) {
		synchronized (applicationReplyHandler) {
			applicationReplyHandler.handleResponse(message.getRequest(),
					message.getReply());

		}

	}

	/**
	 * Increment receive sequence number.
	 */
	public void incrementReceiveSequenceNumber() {
		receiveSequenceNumber++;
	}

	/**
	 * Increment send sequence number.
	 */
	public void incrementSendSequenceNumber() {
		sendSequenceNumber++;
	}

	/**
	 * Read responses.
	 *
	 * @param request
	 *            the request
	 * @return the reply
	 */
	public Reply readResponses(Request request) {
		synchronized (applicationReplyHandler) {
			return applicationReplyHandler.getResponseForRequestId(request);
		}
	}
}
