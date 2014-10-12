package async.chainreplication.client;

import java.util.Map;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.generic.message.queue.MessageQueue;

public class ClientChainReplicationFacade {
	MessageQueue<ChainReplicationMessage> messages = 
			new MessageQueue<ChainReplicationMessage>();
	ClientMessageHandler clientMessageHandler;
	ClientImpl clientImpl;

	public ClientChainReplicationFacade(
			Client client,
			Map<String, Chain> chainNameToChainMap,
			Master master,
			ClientImpl clientImpl) throws ClientChainReplicationException {
		this.clientMessageHandler = 
				new ClientMessageHandler(client, chainNameToChainMap, master, this);
		this.clientImpl = clientImpl;
	}

	public ClientMessageHandler getClientMessageHandler() {
		return clientMessageHandler;
	}

	
    public void logMessage(String message) {
    	this.clientImpl.logMessage(message);
    }
    
	public void deliverMessage(ChainReplicationMessage message) {
		if(message != null) {
			messages.enqueueMessage(message);
		}
		this.logMessage("Message Delivered"+message.toString());
	}

	public Reply readResponsesForRequest(Request request) {
		return this.clientMessageHandler.readResponses(request);
	}

	public void handleMessage(ChainReplicationMessage message) throws ClientChainReplicationException  {
		if(message instanceof ClientRequestMessage) {
			this.clientMessageHandler.handleClientRequestMessage(
					(ClientRequestMessage)message);
		} else if(message instanceof ResponseOrSyncMessage) {
			this.clientMessageHandler.handleReponseMessage(
					(ResponseOrSyncMessage)message);
		} else if (message instanceof MasterMessage) {
			//TODO: In Phase 3
		}
		this.logMessage("ProcessedMessage:"+message.toString());
	}

}
