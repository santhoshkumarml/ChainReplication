package async.chainreplication.client;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.generic.message.queue.MessageQueue;

public class ClientChainReplicationFacade {
	MessageQueue<ChainReplicationMessage> messages = 
			new MessageQueue<ChainReplicationMessage>();
	ClientMessageHandler clientMessageHandler;

	public ClientChainReplicationFacade() {
		this.clientMessageHandler = 
				new ClientMessageHandler();
	}
	

	public void deliverMessage(ChainReplicationMessage message) {
		messages.enqueueMessage(message);

	}
	
	public Reply readResponsesForRequest(Request request) {
		return this.clientMessageHandler.readResponses(request);
	}

	public void handleMessage(ChainReplicationMessage message)  {
		if(message instanceof ClientRequestMessage) {
			this.clientMessageHandler.handleClientRequestMessage(
					(ClientRequestMessage)message);
		} else if(message instanceof ResponseOrSyncMessage) {
			this.clientMessageHandler.handleReponseMessage(
					(ResponseOrSyncMessage)message);
		} else if (message instanceof MasterMessage) {
			//TODO: In Phase 3
		}
	}

}
