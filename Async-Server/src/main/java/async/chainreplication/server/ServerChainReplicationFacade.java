package async.chainreplication.server;

import java.util.Map;

import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.generic.message.queue.MessageQueue;


public class ServerChainReplicationFacade {

	ServerMessageHandler serverMessageHandler;

	MessageQueue<ChainReplicationMessage> messages = 
			new MessageQueue<ChainReplicationMessage>();

	public ServerChainReplicationFacade(
			Server server, 
			Map<String,Chain> chainNameToChainMap,
			Master master) {
		this.serverMessageHandler = 
				new ServerMessageHandler(server,chainNameToChainMap,master);
	}

	public void deliverMessage(ChainReplicationMessage message) {
		messages.enqueueMessage(message);

	}

	public void handleMessage(ChainReplicationMessage message)  {
		if(message instanceof RequestMessage) {
			this.serverMessageHandler.handleRequestMessage(
					(RequestMessage)message);
		} else if(message instanceof ResponseOrSyncMessage) {
			this.serverMessageHandler.handleSyncMessage((ResponseOrSyncMessage) message);
		} else if(message instanceof AckMessage) {
			this.serverMessageHandler.handleAckMessage((AckMessage) message);
		}
	}

	public boolean isHeadInTheChain() {
		return this.serverMessageHandler.getServer().isHead();
	}
	public boolean isTailInTheChain() {
		return this.serverMessageHandler.getServer().isTail();
	}

	public Server getServer() {
		return this.serverMessageHandler.getServer();
	}
	public Master getMaster() {
		return this.serverMessageHandler.getMaster();
	}
}
