package async.chainreplication.client;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.communication.message.models.MasterMessage;
import async.chainreplication.communication.message.models.ResponseOrSyncMessage;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.generic.message.queue.models.MessageQueue;

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

	public void handleMessage(ChainReplicationMessage message)  {
		if(message instanceof ClientRequestMessage) {
			this.clientMessageHandler.handleClientRequestMessage(
					(ClientRequestMessage)message);
		} else if(message instanceof ResponseOrSyncMessage) {
		//	this.clientMessageHandler.handleReponseMessage(
			//		(ResponseOrSyncMessage)message);
		} else if (message instanceof MasterMessage) {
			//TODO: In Phase 3
		}
	}

}
