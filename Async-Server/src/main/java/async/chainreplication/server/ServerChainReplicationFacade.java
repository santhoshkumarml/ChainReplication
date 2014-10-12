package async.chainreplication.server;

import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.generic.message.queue.MessageQueue;


public class ServerChainReplicationFacade {

	ChainReplicationMessageHandler chainReplicationMessageHandler;

	MessageQueue<ChainReplicationMessage> messages = 
			new MessageQueue<ChainReplicationMessage>();

	public ServerChainReplicationFacade(Server server, Master master) {
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
		} else if(message instanceof ResponseOrSyncMessage) {
			this.chainReplicationMessageHandler.handleSyncMessage((ResponseOrSyncMessage) message);
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
