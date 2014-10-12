package async.chainreplication.server;

import async.chainreplication.communication.message.models.AckMessage;
import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.communication.message.models.RequestMessage;
import async.chainreplication.communication.message.models.SyncMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.generic.message.queue.models.MessageQueue;


public class ChainReplicationFacade {

	ChainReplicationMessageHandler chainReplicationMessageHandler;

	MessageQueue<ChainReplicationMessage> messages = 
			new MessageQueue<ChainReplicationMessage>();

	public ChainReplicationFacade(Server server, Master master) {
		this.chainReplicationMessageHandler = 
				new ChainReplicationMessageHandler(server, master);
	}

	public void deliverMessage(ChainReplicationMessage message) {
		messages.enqueueMessage(message);

	}

	public void handleMessage(ChainReplicationMessage message)  {
		if(message instanceof RequestMessage) {
			this.chainReplicationMessageHandler.handleRequestMessage(
					(RequestMessage)message);
		} else if(message instanceof SyncMessage) {
			this.chainReplicationMessageHandler.handleSyncMessage((SyncMessage) message);
		} else if(message instanceof AckMessage) {
			this.chainReplicationMessageHandler.handleAckMessage((AckMessage) message);
		}
	}

	public boolean isHeadInTheChain() {
		return this.chainReplicationMessageHandler.getServer().isHead();
	}
	public boolean isTailInTheChain() {
		return this.chainReplicationMessageHandler.getServer().isTail();
	}

	public Server getServer() {
		return this.chainReplicationMessageHandler.getServer();
	}
	public Master getMaster() {
		return this.chainReplicationMessageHandler.getMaster();
	}
}
