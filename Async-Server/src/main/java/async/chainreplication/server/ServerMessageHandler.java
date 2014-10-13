package async.chainreplication.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
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

public class ServerMessageHandler {
	Request currentRequest;
	Reply currentReply;
	SentHistory sentHistory;
	HistoryOfRequests historyOfRequests;
	Server server;
	Master master;
	Map<String,Chain> chainNameToChainMap = new HashMap<String, Chain>(); 

	IApplicationRequestHandler applicationRequestHandler;

	IClientHelper syncOrAckSendClientHelper;
	IClientHelper tailResponseClientHelper;

	ServerChainReplicationFacade serverChainReplicationFacade;

	public ServerMessageHandler(
			Server server, 
			Map<String,Chain> chainNameToChainMap,
			Master master,
			ServerChainReplicationFacade serverChainReplicationFacade) throws ServerChainReplicationException {
		this.server  = server;
		this.chainNameToChainMap.putAll(chainNameToChainMap);
		this.master =  master;
		this.serverChainReplicationFacade = serverChainReplicationFacade;
		try {
			this.applicationRequestHandler = (IApplicationRequestHandler) Class.forName(
					"async.chainreplication."
							+ "app.server."
							+ "handler.ApplicationRequestHandler").getConstructor(
									ServerMessageHandler.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException |
				InvocationTargetException | NoSuchMethodException |
				SecurityException e) {
			throw new ServerChainReplicationException(e);
		}

	}

	public Request getCurrentRequest() {
		return currentRequest;
	}

	public void setCurrentRequest(Request currentRequest) {
		this.currentRequest = currentRequest;
	}

	public Reply getCurrentReply() {
		return currentReply;
	}

	public void setCurrentReply(Reply currentReply) {
		this.currentReply = currentReply;
	}

	public SentHistory getSentHistory() {
		return sentHistory;
	}

	public void setSentHistory(SentHistory sentHistory) {
		this.sentHistory = sentHistory;
	}

	public HistoryOfRequests getHistoryOfRequests() {
		return historyOfRequests;
	}

	public void setHistoryOfRequests(HistoryOfRequests historyOfRequests) {
		this.historyOfRequests = historyOfRequests;
	}


	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}




	//---------------------------------------------------------------------------------
	//Handle Chain Operation

	public void sync(Request request, Reply reply) throws ServerChainReplicationException {
		this.setCurrentRequest(request);
		this.setCurrentReply(reply);
		historyOfRequests.addToHistory(request);
		Server sucessor = this.server.getAdjacencyList().getSucessor();
		if(sucessor != null) {
			//Non tail operation is to sync
			synchronized (syncOrAckSendClientHelper) {
				syncOrAckSendClientHelper = new TCPClientHelper(
						sucessor.getServerProcessDetails().getHost(),
						sucessor.getServerProcessDetails().getTcpPort());
				ChainReplicationMessage syncMessage = new ResponseOrSyncMessage(request, reply);
				try {
					syncOrAckSendClientHelper.sendMessage(syncMessage);
				} catch (ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}	
			}
			sentHistory.addToSentHistory(request.getRequestId());
			//send sync
		} else {
			//Tail operation reply
			//TODO Change here for Transfer Have to wait for ACK before reply
			tailResponseClientHelper = new UDPClientHelper(
					request.getClient().getClientProcessDetails().getHost()	,
					request.getClient().getClientProcessDetails().getUdpPort());
			try {
				tailResponseClientHelper.sendMessage(reply);
			} catch (ConnectClientException e) {
				throw new ServerChainReplicationException(e);
			}
			//ACk so that other servers can remove the messages from Sent
			ACK(request);
		}
	}

	public void ACK(Request request) throws ServerChainReplicationException {
		sentHistory.removeFromSent(request.getRequestId());
		Server predecessor = this.server.getAdjacencyList().getPredecessor();
		//Terminate propagation once we reach head
		if(predecessor != null) {
			synchronized (syncOrAckSendClientHelper) {
				syncOrAckSendClientHelper = new TCPClientHelper(
						predecessor.getServerProcessDetails().getHost(),
						predecessor.getServerProcessDetails().getTcpPort());
				ChainReplicationMessage ackMessage = new AckMessage(request);
				//change it to ACK Message
				try {
					syncOrAckSendClientHelper.sendMessage(ackMessage);
				} catch (ConnectClientException e) {
					throw new ServerChainReplicationException(e);
				}	
			}
		}

	}

	/*public void IN_TRANSIT_UPDATES(String lastRequestId) {
	synchronized(sentHistory) {
		for(String requestId : sentHistory.getRequestIds()) {
			//get latest Value for the account and return it
		}
	}
}*/


	//-------------------------------------------------------------------------------------
	//Message Handle Methods

	public void handleRequestMessage(RequestMessage message) throws ServerChainReplicationException {
		Reply reply = this.applicationRequestHandler.handleRequest(message.getRequest());
		sync(message.getRequest(), reply);
	}

	public void handleSyncMessage(ResponseOrSyncMessage message) throws ServerChainReplicationException {
		this.applicationRequestHandler.handleSyncUpdate(message.getReply());
		sync(message.getRequest(), message.getReply());
	}

	public void handleAckMessage(AckMessage message) throws ServerChainReplicationException {
		ACK(message.getRequest());
	}

}
