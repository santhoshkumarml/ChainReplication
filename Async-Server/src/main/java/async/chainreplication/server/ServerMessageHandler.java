package async.chainreplication.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.server.communication.models.RequestKey;
import async.chainreplication.client.server.communication.models.RequestType;
import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ApplicationMessage;
import async.chainreplication.communication.messages.BulkSyncMessage;
import async.chainreplication.communication.messages.ChainJoinMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterChainJoinReplyMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
import async.chainreplication.communication.messages.NewNodeInitializedMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.communication.messages.SuccessorRequestMessage;
import async.chainreplication.communication.messages.WaitServerMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.chainreplication.server.models.HistoryOfRequests;
import async.chainreplication.server.models.SentHistory;
import async.connection.util.ConnectClientException;
import async.connection.util.IClientHelper;
import async.connection.util.TCPClientHelper;
import async.connection.util.UDPClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerMessageHandler.
 */
public class ServerMessageHandler {

	/**
	 * The Class NewNodeUpdater.
	 */
	private static class NewNodeUpdater extends Thread {

		/** The transactional application objects. */
		Set<?> transactionalApplicationObjects;

		/** The new node client helper. */
		IClientHelper newNodeClientHelper;

		/** The message handler. */
		ServerMessageHandler messageHandler;

		/**
		 * Instantiates a new new node updater.
		 *
		 * @param transactionalApplicationObjects
		 *            the transactional application objects
		 * @param server
		 *            the server
		 * @param serverMessageHandler
		 *            the server message handler
		 */
		public NewNodeUpdater(Set<?> transactionalApplicationObjects,
				Server server, ServerMessageHandler serverMessageHandler) {
			this.transactionalApplicationObjects = transactionalApplicationObjects;
			newNodeClientHelper = new TCPClientHelper(server
					.getServerProcessDetails().getHost(), server
					.getServerProcessDetails().getTcpPort());
			messageHandler = serverMessageHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			messageHandler.setCanSendAck(false);
			synchronized (newNodeClientHelper) {
				try {
					for (final Object transactionalObject : transactionalApplicationObjects) {
						final ApplicationMessage message = new ApplicationMessage(
								transactionalObject, false);
						messageHandler
								.sendMessage(newNodeClientHelper, message);

					}

					final HistoryOfRequests historyOfRequests = messageHandler
							.getHistoryOfRequests();
					final SentHistory sentHistory = messageHandler
							.getSentHistory();
					Set<RequestKey> requestKeys = new HashSet<RequestKey>();
					int greatestSequenceNumber = 0;
					synchronized (historyOfRequests) {
						requestKeys = historyOfRequests.listRequestKeys();
						greatestSequenceNumber = historyOfRequests
								.getGreatestSequenceNumberReceived();
					}

					for (final RequestKey requestKey : requestKeys) {
						final Request request = historyOfRequests
								.getExisistingRequest(requestKey);
						final Reply reply = historyOfRequests
								.getExisistingReply(requestKey);
						final ResponseOrSyncMessage syncMessage = new ResponseOrSyncMessage(
								request, reply);
						messageHandler.sendMessage(newNodeClientHelper,
								syncMessage);
					}
					synchronized (sentHistory) {
						final BulkSyncMessage bulkSyncMessage = new BulkSyncMessage();
						for (final RequestKey requestKey : sentHistory
								.getRequestKeysFromSent(greatestSequenceNumber)) {
							final ResponseOrSyncMessage syncMessage = new ResponseOrSyncMessage(
									historyOfRequests
											.getExisistingRequest(requestKey),
									historyOfRequests
											.getExisistingReply(requestKey));
							bulkSyncMessage.getSyncMessages().add(syncMessage);
							messageHandler.sendMessage(newNodeClientHelper,
									syncMessage);
						}
					}
					final ApplicationMessage message = new ApplicationMessage(
							null, true);
					messageHandler.sendMessage(newNodeClientHelper, message);
				} catch (final ConnectClientException e) {
					messageHandler.logMessage(e.getMessage());
				}
				messageHandler.setCanSendAck(true);
			}
		}
	}

	/** The current request. */
	Request currentRequest;

	/** The current reply. */
	Reply currentReply;

	/** The sent history. */
	SentHistory sentHistory = new SentHistory();

	/** The history of requests. */
	HistoryOfRequests historyOfRequests = new HistoryOfRequests();

	/** The server. */
	Server server;

	/** The master. */
	Master master;

	/** The chain name to chain map. */
	Map<String, Chain> chainNameToChainMap = new HashMap<String, Chain>();

	/** The application request handler. */
	IApplicationRequestHandler applicationRequestHandler;

	/** The sync or ack send client helper. */
	IClientHelper peerSendClientHelper;

	/** The tail response client helper. */
	IClientHelper tailResponseClientHelper;

	/** The server chain replication facade. */
	ServerChainReplicationFacade serverChainReplicationFacade;

	/** The send sequence number. */
	volatile int sendSequenceNumber = 0;

	/** The receive sequence number. */
	volatile int receiveSequenceNumber = 0;

	/** The can send ack. */
	volatile boolean canSendAck = true;

	/**
	 * Instantiates a new server message handler.
	 *
	 * @param server
	 *            the server
	 * @param chainNameToChainMap
	 *            the chain name to chain map
	 * @param master
	 *            the master
	 * @param serverChainReplicationFacade
	 *            the server chain replication facade
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public ServerMessageHandler(Server server,
			Map<String, Chain> chainNameToChainMap, Master master,
			ServerChainReplicationFacade serverChainReplicationFacade)
			throws ServerChainReplicationException {
		this.server = server;
		this.chainNameToChainMap.putAll(chainNameToChainMap);
		this.master = master;
		this.serverChainReplicationFacade = serverChainReplicationFacade;
		try {
			applicationRequestHandler = (IApplicationRequestHandler) Class
					.forName(
							"async.chainreplication." + "app.server."
									+ "handler.ApplicationRequestHandler")
					.getConstructor(ServerMessageHandler.class)
					.newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new ServerChainReplicationException(e);
		}

	}

	/**
	 * Ack.
	 *
	 * @param request
	 *            the request
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void ACK(Request request) throws ServerChainReplicationException {
		final Server predecessor = server.getAdjacencyList().getPredecessor();
		applicationRequestHandler.handleAck(request);
		// Terminate propagation once we reach head
		if (predecessor != null) {
			peerSendClientHelper = new TCPClientHelper(predecessor
					.getServerProcessDetails().getHost(), predecessor
					.getServerProcessDetails().getTcpPort());
			synchronized (peerSendClientHelper) {
				final ChainReplicationMessage ackMessage = new AckMessage(
						request);
				// change it to ACK Message
				peerSendClientHelper = new TCPClientHelper(predecessor
						.getServerProcessDetails().getHost(), predecessor
						.getServerProcessDetails().getTcpPort());
				try {
					sendMessage(peerSendClientHelper, ackMessage);
				} catch (final ConnectClientException e) {
					this.logMessage(e.getMessage());
				}
				/*
				 * incrementSendSequenceNumber();
				 * serverChainReplicationFacade.logMessage("Outgoing Message-" +
				 * sendSequenceNumber + ":" + ackMessage.toString());
				 */
			}
		}
	}

	/**
	 * Gets the current reply.
	 *
	 * @return the current reply
	 */
	public Reply getCurrentReply() {
		return currentReply;
	}

	/**
	 * Gets the current request.
	 *
	 * @return the current request
	 */
	public Request getCurrentRequest() {
		return currentRequest;
	}

	/**
	 * Gets the history of requests.
	 *
	 * @return the history of requests
	 */
	public HistoryOfRequests getHistoryOfRequests() {
		return historyOfRequests;
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
	 * Gets the sent history.
	 *
	 * @return the sent history
	 */
	public SentHistory getSentHistory() {
		return sentHistory;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Handle ack message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleAckMessage(AckMessage message)
			throws ServerChainReplicationException {
		ACK(message.getRequest());
	}

	/**
	 * Handle application message.
	 *
	 * @param message
	 *            the message
	 * @throws ConnectClientException
	 *             the connect client exception
	 */
	public void handleApplicationMessage(ApplicationMessage message)
			throws ConnectClientException {
		if (!message.isLastMessage()) {
			applicationRequestHandler.updateTransactionalObject(message);
		} else {
			final NewNodeInitializedMessage newNodeInitializedMessage = new NewNodeInitializedMessage(
					server);
			final IClientHelper masterContacter = new TCPClientHelper(
					master.getMasterHost(), master.getMasterPort());
			sendMessage(masterContacter, newNodeInitializedMessage);
		}
	}

	/**
	 * Handle chain join message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleChainJoinMessage(ChainJoinMessage message)
			throws ServerChainReplicationException {
		final Server server = message.getServer();
		final NewNodeUpdater updater = new NewNodeUpdater(
				applicationRequestHandler.getTransactionalObjects(), server,
				this);
		updater.start();
	}

	/**
	 * Handle chain join reply message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleChainJoinReplyMessage(MasterChainJoinReplyMessage message)
			throws ServerChainReplicationException {
		if (message.getExisistingTail() == null) {
			final NewNodeInitializedMessage newNodeInitializedMessage = new NewNodeInitializedMessage(
					server);
			final IClientHelper masterContacter = new TCPClientHelper(
					master.getMasterHost(), master.getMasterPort());
			try {
				sendMessage(masterContacter, newNodeInitializedMessage);
			} catch (final ConnectClientException e) {
				throw new ServerChainReplicationException(e);
			}
		} else {
			final ChainJoinMessage joinMessageToServer = new ChainJoinMessage(
					server);
			final IClientHelper exisistingTailContacter = new TCPClientHelper(
					message.getExisistingTail().getServerProcessDetails()
							.getHost(), message.getExisistingTail()
							.getServerProcessDetails().getTcpPort());
			try {
				sendMessage(exisistingTailContacter, joinMessageToServer);
			} catch (final ConnectClientException e) {
				throw new ServerChainReplicationException(e);
			}
		}
	}

	/**
	 * Handle master message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleMasterMessage(MasterServerChangeMessage message)
			throws ServerChainReplicationException {
		final Server newServerObject = message.getServer();
		synchronized (server) {
			/*
			 * if(server.getAdjacencyList().getSucessor() !=
			 * newServerObject.getAdjacencyList().getSucessor()) {
			 * WaitServerMessage waitServerMessage = new
			 * WaitServerMessage(SuccessorRequestMessage.class);
			 * this.serverChainReplicationFacade
			 * .deliverMessage(waitServerMessage); }
			 */
			if (server.getAdjacencyList().getPredecessor() != newServerObject
					.getAdjacencyList().getPredecessor()) {
				final int lastSequenceNumberReceived = this
						.getHistoryOfRequests()
						.getGreatestSequenceNumberReceived();
				final SuccessorRequestMessage successorRequestMessage = new SuccessorRequestMessage(
						lastSequenceNumberReceived);
				final Server predecessor = newServerObject.getAdjacencyList()
						.getPredecessor();
				if (predecessor != null) {
					peerSendClientHelper = new TCPClientHelper(predecessor
							.getServerProcessDetails().getHost(), predecessor
							.getServerProcessDetails().getTcpPort());
					synchronized (peerSendClientHelper) {
						try {
							sendMessage(peerSendClientHelper,
									successorRequestMessage);
						} catch (final ConnectClientException e) {
							this.logMessage(e.getMessage());
						}
					}
				}
			}
			server = newServerObject;
		}
		if (!message.getOtherChains().isEmpty()) {
			final Set<Chain> chains = message.getOtherChains();
			synchronized (chainNameToChainMap) {
				for (final Chain chain : chains) {
					chainNameToChainMap.put(chain.getChainName(), chain);
				}
			}
		}
	}

	/**
	 * Handle request message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleRequestMessage(RequestMessage message)
			throws ServerChainReplicationException {
		final Reply reply = applicationRequestHandler.handleRequest(message
				.getRequest());
		sync(message.getRequest(), reply);
	}

	/**
	 * Handle start server.
	 *
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleStartServer() throws ServerChainReplicationException {
		final ChainJoinMessage joinMessage = new ChainJoinMessage(server);
		final IClientHelper masterContacter = new TCPClientHelper(
				master.getMasterHost(), master.getMasterPort());
		try {
			sendMessage(masterContacter, joinMessage);
		} catch (final ConnectClientException e) {
			throw new ServerChainReplicationException(e);
		}
	}

	// ---------------------------------------------------------------------------------
	// Handle Chain Operation

	/**
	 * Handle successor request message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleSuccessorRequestMessage(SuccessorRequestMessage message)
			throws ServerChainReplicationException {
		final Server sucessor = server.getAdjacencyList().getSucessor();
		if (sucessor != null) {
			peerSendClientHelper = new TCPClientHelper(sucessor
					.getServerProcessDetails().getHost(), sucessor
					.getServerProcessDetails().getTcpPort());
			synchronized (peerSendClientHelper) {
				final int lastSequenceNumberReceived = message
						.getLastSequenceNumberReceived();
				final BulkSyncMessage bulkSyncMessage = new BulkSyncMessage();
				for (final RequestKey requestKey : this.getSentHistory()
						.getRequestKeysFromSent(lastSequenceNumberReceived)) {
					final ResponseOrSyncMessage syncMessage = new ResponseOrSyncMessage(
							this.getHistoryOfRequests().getExisistingRequest(
									requestKey), this.getHistoryOfRequests()
									.getExisistingReply(requestKey));
					bulkSyncMessage.getSyncMessages().add(syncMessage);
				}
				try {
					sendMessage(peerSendClientHelper, bulkSyncMessage);
				} catch (final ConnectClientException e) {
					this.logMessage(e.getMessage());
				}
			}
		}
	}

	/**
	 * Handle sync message.
	 *
	 * @param message
	 *            the message
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void handleSyncMessage(ResponseOrSyncMessage message)
			throws ServerChainReplicationException {
		sync(message.getRequest(), message.getReply());
	}

	/*
	 * public void IN_TRANSIT_UPDATES(String lastRequestId) {
	 * synchronized(sentHistory) { for(String requestId :
	 * sentHistory.getRequestIds()) { //get latest Value for the account and
	 * return it } } }
	 */

	// -------------------------------------------------------------------------------------
	// Message Handle Methods

	/**
	 * Handle wait server message.
	 *
	 * @param message
	 *            the message
	 */
	public void handleWaitServerMessage(WaitServerMessage message) {
		while (serverChainReplicationFacade.getMessageQueue().peekAtMessage()
				.getMessageObject().getClass() != message.getClass())
			;
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
	 * Log message.
	 *
	 * @param message
	 *            the message
	 */
	private void logMessage(String message) {
		serverChainReplicationFacade.logMessage(message);
	}

	/**
	 * Send message.
	 *
	 * @param client
	 *            the client
	 * @param chainReplicationMessage
	 *            the chain replication message
	 * @throws ConnectClientException
	 *             the connect client exception
	 */
	public void sendMessage(IClientHelper client,
			ChainReplicationMessage chainReplicationMessage)
			throws ConnectClientException {
		// TODO Remove this later
		this.logMessage("Sending Message:" + chainReplicationMessage.toString());
		client.sendMessage(chainReplicationMessage);
	}

	/**
	 * Sets the can send ack.
	 *
	 * @param canSendAck
	 *            the new can send ack
	 */
	private void setCanSendAck(boolean canSendAck) {
		this.canSendAck = canSendAck;
	}

	/**
	 * Sets the current reply.
	 *
	 * @param currentReply
	 *            the new current reply
	 */
	public void setCurrentReply(Reply currentReply) {
		this.currentReply = currentReply;
	}

	/**
	 * Sets the current request.
	 *
	 * @param currentRequest
	 *            the new current request
	 */
	public void setCurrentRequest(Request currentRequest) {
		this.currentRequest = currentRequest;
	}

	/**
	 * Sets the history of requests.
	 *
	 * @param historyOfRequests
	 *            the new history of requests
	 */
	public void setHistoryOfRequests(HistoryOfRequests historyOfRequests) {
		this.historyOfRequests = historyOfRequests;
	}

	/**
	 * Sets the master.
	 *
	 * @param master
	 *            the new master
	 */
	public void setMaster(Master master) {
		this.master = master;
	}

	/**
	 * Sets the sent history.
	 *
	 * @param sentHistory
	 *            the new sent history
	 */
	public void setSentHistory(SentHistory sentHistory) {
		this.sentHistory = sentHistory;
	}

	/**
	 * Sets the server.
	 *
	 * @param server
	 *            the new server
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * Sync.
	 *
	 * @param request
	 *            the request
	 * @param reply
	 *            the reply
	 * @throws ServerChainReplicationException
	 *             the server chain replication exception
	 */
	public void sync(Request request, Reply reply)
			throws ServerChainReplicationException {
		this.setCurrentRequest(request);
		this.setCurrentReply(reply);
		applicationRequestHandler.handleSyncUpdate(request, reply);
		final Server sucessor = server.getAdjacencyList().getSucessor();
		if (sucessor != null) {
			// Non tail operation is to sync
			peerSendClientHelper = new TCPClientHelper(sucessor
					.getServerProcessDetails().getHost(), sucessor
					.getServerProcessDetails().getTcpPort());
			synchronized (peerSendClientHelper) {
				final ChainReplicationMessage syncMessage = new ResponseOrSyncMessage(
						request, reply);
				try {
					sendMessage(peerSendClientHelper, syncMessage);
				} catch (final ConnectClientException e) {
					this.logMessage(e.getMessage());
				}
				/*
				 * incrementSendSequenceNumber();
				 * serverChainReplicationFacade.logMessage("Outgoing Message-" +
				 * sendSequenceNumber + ":" + syncMessage.toString());
				 */
			}
			// send sync
		} else {
			// Tail operation reply
			// TODO Change here for Transfer Have to wait for ACK before reply
			tailResponseClientHelper = new UDPClientHelper(request.getClient()
					.getClientProcessDetails().getHost(), request.getClient()
					.getClientProcessDetails().getUdpPort());
			synchronized (tailResponseClientHelper) {
				final ChainReplicationMessage responseMessage = new ResponseOrSyncMessage(
						request, reply);
				try {
					sendMessage(tailResponseClientHelper, responseMessage);
				} catch (final ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}
				// TODO uncomment this later
				/*
				 * incrementSendSequenceNumber(); serverChainReplicationFacade
				 * .logMessage("Outgoing Message-" + sendSequenceNumber + ":" +
				 * responseMessage.toString());
				 */
			}

			// ACk so that other servers can remove the messages from Sent
			if (canSendAck && request.getRequestType() != RequestType.QUERY) {
				ACK(request);
			}
		}
	}
}