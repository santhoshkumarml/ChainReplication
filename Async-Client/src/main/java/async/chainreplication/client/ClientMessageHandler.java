package async.chainreplication.client;

import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.connection.util.IClientHelper;
import async.connection.util.UDPClientHelper;

public class ClientMessageHandler {

	Client client;
	Map<String, Chain> chainNameToChainMap = new HashMap<String, Chain>();
	Master master;
	IClientHelper clientMessageClientHelper;
	IApplicationReplyHandler applicationReplyHandler;

	public ClientMessageHandler(Client client,
			Map<String, Chain> chainNameToChainMap, Master master) {
		this.client = client;
		this.chainNameToChainMap.putAll(chainNameToChainMap);
		this.master = master;
		try {
			this.applicationReplyHandler = (IApplicationReplyHandler) Class.forName(
					"async.chainreplication.app.client."
							+ "handler.ApplicationReplyHandler").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
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

	//---------------------------------------------------------------------------------
	//Client Handler Methods
	public void handleClientRequestMessage(ClientRequestMessage message) {
		String chainName = message.getChainName();
		switch(message.getRequestMessage().getRequest().getRequestType()) {
		case QUERY:
			Server tail = getTailForChain(chainName);
			clientMessageClientHelper = new UDPClientHelper(
					tail.getServerProcessDetails().getHost(),
					tail.getServerProcessDetails().getPort());
			break;
		case UPDATE:
			Server head = getHeadForChain(chainName);
			clientMessageClientHelper = new UDPClientHelper(
					head.getServerProcessDetails().getHost(),
					head.getServerProcessDetails().getPort());
			break;
		}
		clientMessageClientHelper.sendMessage(message.getRequestMessage());
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
