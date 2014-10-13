package async.chainreplication.client;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.connection.util.ConnectClientException;
import async.connection.util.IClientHelper;
import async.connection.util.UDPClientHelper;

public class ClientMessageHandler {

	Client client;
	Map<String, Chain> chainNameToChainMap = new HashMap<String, Chain>();
	Master master;
	IClientHelper clientMessageClientHelper;
	IApplicationReplyHandler applicationReplyHandler;
	ClientChainReplicationFacade clientChainReplicationFacade;
	
	volatile int sendSequenceNumber = 0;
	volatile int receiveSequenceNumber = 0;

	public ClientMessageHandler(Client client,
			Map<String, Chain> chainNameToChainMap, Master master,
			ClientChainReplicationFacade clientChainReplicationFacade) throws ClientChainReplicationException {
		this.client = client;
		this.chainNameToChainMap.putAll(chainNameToChainMap);
		this.master = master;
		this.clientChainReplicationFacade = clientChainReplicationFacade;

		try {
			this.applicationReplyHandler = (IApplicationReplyHandler) Class.forName(
					"async.chainreplication.app.client."
							+ "handler.ApplicationReplyHandler").getConstructor(
									ClientMessageHandler.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ClientChainReplicationException(e);
		}
	}

	public ClientChainReplicationFacade getClientChainReplicationFacade() {
		return clientChainReplicationFacade;
	}
	
	public void incrementSendSequenceNumber() {
		sendSequenceNumber++;
	}
	
	public void incrementReceiveSequenceNumber() {
		receiveSequenceNumber++;
	}
	
	public int getSendSequenceNumber() {
		return sendSequenceNumber;
	}

	public int getReceiveSequenceNumber() {
		return receiveSequenceNumber;
	}

	private Server getHeadForChain(String chainName) {
		synchronized (chainNameToChainMap) {
			return this.chainNameToChainMap.get(chainName).getHead();	
		}
	}

	private Server getTailForChain(String chainName) {
		synchronized (chainNameToChainMap) {
			return this.chainNameToChainMap.get(chainName).getTail();	
		}
	}

	public Client getClient() {
		return this.client;
	}

	public Master getMaster() {
		return this.master;
	}

	//---------------------------------------------------------------------------------
	//Client Handler Methods
	public void handleClientRequestMessage(ClientRequestMessage message) throws ClientChainReplicationException {
		String chainName = message.getChainName();
		switch(message.getRequestMessage().getRequest().getRequestType()) {
		case QUERY:
			Server tail = getTailForChain(chainName);
			clientMessageClientHelper = new UDPClientHelper(
					tail.getServerProcessDetails().getHost(),
					tail.getServerProcessDetails().getUdpPort());
			break;
		case UPDATE:
			Server head = getHeadForChain(chainName);
			clientMessageClientHelper = new UDPClientHelper(
					head.getServerProcessDetails().getHost(),
					head.getServerProcessDetails().getUdpPort());
			break;
		}
		try {
			clientMessageClientHelper.sendMessage(message.getRequestMessage());
		} catch (ConnectClientException e) {
			throw new ClientChainReplicationException(e);
		}
		incrementSendSequenceNumber();
		this.getClientChainReplicationFacade().logMessage("Outgoing Message-"+this.sendSequenceNumber+":"+ message.toString());
	}

	public void handleReponseMessage(ResponseOrSyncMessage message) {
		synchronized (applicationReplyHandler) {
			this.applicationReplyHandler.handleResponse(
					message.getRequest(),
					message.getReply());

		}

	}

	public Reply readResponses(Request request) {
		synchronized (applicationReplyHandler) {
			return this.applicationReplyHandler.getResponseForRequestId(request);
		}		
	}


}
