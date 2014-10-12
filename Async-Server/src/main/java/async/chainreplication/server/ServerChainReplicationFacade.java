package async.chainreplication.server;

import java.util.Map;

import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.generic.message.queue.MessageQueue;


public class ServerChainReplicationFacade {

	ServerMessageHandler serverMessageHandler;

	MessageQueue<ChainReplicationMessage> messages = 
			new MessageQueue<ChainReplicationMessage>();
	
	ServerImpl serverImpl;

	public ServerChainReplicationFacade(
			Server server, 
			Map<String,Chain> chainNameToChainMap,
			Master master, ServerImpl serverImpl) throws ServerChainReplicationException {
		this.serverMessageHandler = 
				new ServerMessageHandler(server,
						chainNameToChainMap, 
						master, this);
		this.serverImpl = serverImpl;
	}
	
	
	public void logMessage(String message) {
		this.serverImpl.logMessage(message);
	}

	public void deliverMessage(ChainReplicationMessage message) {
		if(message != null) {
			messages.enqueueMessage(message);
		}
		this.logMessage("Message Delivered"+message.toString());

	}

	public void handleMessage(ChainReplicationMessage message) throws ServerChainReplicationException  {
		if(message instanceof RequestMessage) {
			this.serverMessageHandler.handleRequestMessage(
					(RequestMessage)message);
		} else if(message instanceof ResponseOrSyncMessage) {
			this.serverMessageHandler.handleSyncMessage((ResponseOrSyncMessage) message);
		} else if(message instanceof AckMessage) {
			this.serverMessageHandler.handleAckMessage((AckMessage) message);
		}
		this.logMessage("ProcessedMessage:"+message.toString());
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
