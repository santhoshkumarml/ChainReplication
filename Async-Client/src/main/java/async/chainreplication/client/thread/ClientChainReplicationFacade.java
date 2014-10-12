package async.chainreplication.client.thread;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.generic.message.queue.models.MessageQueue;

public class ClientChainReplicationFacade {
	MessageQueue<ChainReplicationMessage> masterMessages = 
			new MessageQueue<ChainReplicationMessage>();

}
