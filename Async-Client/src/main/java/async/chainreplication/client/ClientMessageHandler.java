package async.chainreplication.client;

import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Server;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.connection.util.IClientHelper;
import async.connection.util.UDPClientHelper;

public class ClientMessageHandler {

	Map<String, Chain> chainNameToChainMap = new HashMap<String, Chain>();
	IClientHelper clientMessageClientHelper;
	IApplicationReplyHandler applicationReplyHandler;

	public ClientMessageHandler() {
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

	public void readResponses(Request request) {
		synchronized (applicationReplyHandler) {
			this.applicationReplyHandler.getResponseForRequestId(request);
		}		
	}

}
