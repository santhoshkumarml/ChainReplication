package async.chainreplication.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.server.communication.models.RequestKey;
import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.BulkSyncMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
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

	/**
	 * Instantiates a new server message handler.
	 *
	 * @param server the server
	 * @param chainNameToChainMap the chain name to chain map
	 * @param master the master
	 * @param serverChainReplicationFacade the server chain replication facade
	 * @throws ServerChainReplicationException the server chain replication exception
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
	 * @param request the request
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public void ACK(Request request) throws ServerChainReplicationException {
		Server predecessor = server.getAdjacencyList().getPredecessor();
		applicationRequestHandler.handleAck(request);
		// Terminate propagation once we reach head
		if (predecessor != null) {
			peerSendClientHelper = new TCPClientHelper(predecessor
					.getServerProcessDetails().getHost(), predecessor
					.getServerProcessDetails().getTcpPort());
			synchronized (peerSendClientHelper) {
				ChainReplicationMessage ackMessage = new AckMessage(request);
				// change it to ACK Message
				try {
					peerSendClientHelper.sendMessage(ackMessage);
				} catch (ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}
				incrementSendSequenceNumber();
				serverChainReplicationFacade.logMessage("Outgoing Message-"
						+ sendSequenceNumber + ":" + ackMessage.toString());
			}
		}
		applicationRequestHandler.handleAck(request);
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
	 * @param message the message
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public void handleAckMessage(AckMessage message)
			throws ServerChainReplicationException {
		ACK(message.getRequest());
	}

	/**
	 * Handle master message.
	 *
	 * @param message the message
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public void handleMasterMessage(MasterServerChangeMessage message) throws ServerChainReplicationException {
		Server newServerObject = message.getServer();
		if (!server.getAdjacencyList().getSucessor()
				.equals(newServerObject.getAdjacencyList().getSucessor())) {
			// get Ready for sending SentHistory asynchronously
		}
		synchronized (server) {
			if(server.getAdjacencyList().getPredecessor() !=
					newServerObject.getAdjacencyList().getPredecessor()) {
				int lastSequenceNumberReceived = this.getHistoryOfRequests().getGreatestSequenceNumberReceived();
				SuccessorRequestMessage successorRequestMessage = 
						new SuccessorRequestMessage(lastSequenceNumberReceived);
				Server predecessor = newServerObject.getAdjacencyList().getPredecessor();
				if (predecessor != null) {
					peerSendClientHelper = new TCPClientHelper(predecessor
							.getServerProcessDetails().getHost(), predecessor
							.getServerProcessDetails().getTcpPort());
					synchronized (peerSendClientHelper) {
						// change it to ACK Message
						try {
							peerSendClientHelper.sendMessage(successorRequestMessage);
						} catch (ConnectClientException e) {
							throw new ServerChainReplicationException(e);
						}
					}
				}
			}
			if(server.getAdjacencyList().getSucessor() !=
					newServerObject.getAdjacencyList().getSucessor()) {
				WaitServerMessage waitServerMessage = new WaitServerMessage(SuccessorRequestMessage.class);
				this.serverChainReplicationFacade.deliverMessage(waitServerMessage);
			}
			server = newServerObject;
		}
		if (!message.getOtherChains().isEmpty()) {
			Set<Chain> chains = message.getOtherChains();
			synchronized (chainNameToChainMap) {
				for (Chain chain : chains) {
					chainNameToChainMap.put(chain.getChainName(), chain);
				}
			}
		}
	}

	/**
	 * Handle request message.
	 *
	 * @param message the message
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public void handleRequestMessage(RequestMessage message)
			throws ServerChainReplicationException {
		Reply reply = applicationRequestHandler.handleRequest(message
				.getRequest());
		sync(message.getRequest(), reply);
	}

	/**
	 * Handle sync message.
	 *
	 * @param message the message
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public void handleSyncMessage(ResponseOrSyncMessage message)
			throws ServerChainReplicationException {
		sync(message.getRequest(), message.getReply());
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
	 * Sets the current reply.
	 *
	 * @param currentReply the new current reply
	 */
	public void setCurrentReply(Reply currentReply) {
		this.currentReply = currentReply;
	}

	// ---------------------------------------------------------------------------------
	// Handle Chain Operation

	/**
	 * Sets the current request.
	 *
	 * @param currentRequest the new current request
	 */
	public void setCurrentRequest(Request currentRequest) {
		this.currentRequest = currentRequest;
	}

	/**
	 * Sets the history of requests.
	 *
	 * @param historyOfRequests the new history of requests
	 */
	public void setHistoryOfRequests(HistoryOfRequests historyOfRequests) {
		this.historyOfRequests = historyOfRequests;
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
	 * Sets the master.
	 *
	 * @param master the new master
	 */
	public void setMaster(Master master) {
		this.master = master;
	}

	/**
	 * Sets the sent history.
	 *
	 * @param sentHistory the new sent history
	 */
	public void setSentHistory(SentHistory sentHistory) {
		this.sentHistory = sentHistory;
	}

	/**
	 * Sets the server.
	 *
	 * @param server the new server
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * Sync.
	 *
	 * @param request the request
	 * @param reply the reply
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public void sync(Request request, Reply reply)
			throws ServerChainReplicationException {
		this.setCurrentRequest(request);
		this.setCurrentReply(reply);
		applicationRequestHandler.handleSyncUpdate(request, reply);
		Server sucessor = server.getAdjacencyList().getSucessor();
		if (sucessor != null) {
			// Non tail operation is to sync
			peerSendClientHelper = new TCPClientHelper(sucessor
					.getServerProcessDetails().getHost(), sucessor
					.getServerProcessDetails().getTcpPort());
			synchronized (peerSendClientHelper) {
				ChainReplicationMessage syncMessage = new ResponseOrSyncMessage(
						request, reply);
				try {
					peerSendClientHelper.sendMessage(syncMessage);
				} catch (ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}
				incrementSendSequenceNumber();
				serverChainReplicationFacade.logMessage("Outgoing Message-"
						+ sendSequenceNumber + ":" + syncMessage.toString());
			}
			// send sync
		} else {
			// Tail operation reply
			// TODO Change here for Transfer Have to wait for ACK before reply
			tailResponseClientHelper = new UDPClientHelper(request.getClient()
					.getClientProcessDetails().getHost(), request.getClient()
					.getClientProcessDetails().getUdpPort());
			synchronized (tailResponseClientHelper) {
				ChainReplicationMessage responseMessage = new ResponseOrSyncMessage(
						request, reply);
				try {
					tailResponseClientHelper.sendMessage(responseMessage);
				} catch (ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}
				incrementSendSequenceNumber();
				serverChainReplicationFacade
				.logMessage("Outgoing Message-" + sendSequenceNumber
						+ ":" + responseMessage.toString());
			}

			// ACk so that other servers can remove the messages from Sent
			ACK(request);
		}
	}

	public void handleWaitServerMessage(WaitServerMessage message) {
		while(this.serverChainReplicationFacade.
				getMessageQueue().peekAtMessage().getMessageObject().
				getClass() == message.getClass());
	}

	public void handleSuccessorRequestMessage(SuccessorRequestMessage message) throws ServerChainReplicationException {
		Server sucessor = server.getAdjacencyList().getSucessor();
		if (sucessor != null) {
			// Non tail operation is to sync
			peerSendClientHelper = new TCPClientHelper(sucessor
					.getServerProcessDetails().getHost(), sucessor
					.getServerProcessDetails().getTcpPort());
			synchronized (peerSendClientHelper) {
				int lastSequenceNumberReceived = message.getLastSequenceNumberReceived();
				BulkSyncMessage bulkSyncMessage = new BulkSyncMessage();
				for(RequestKey requestKey :
					this.getSentHistory().getRequestKeysFromSent(lastSequenceNumberReceived)) {
					ResponseOrSyncMessage syncMessage = 
							new ResponseOrSyncMessage(
									this.getHistoryOfRequests().getExisistingRequest(requestKey),
									this.getHistoryOfRequests().getExisistingReply(requestKey));
					bulkSyncMessage.getSyncMessages().add(syncMessage);
				}
				try {
					peerSendClientHelper.sendMessage(bulkSyncMessage);
				} catch (ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}
				
			}

		}
	}
}