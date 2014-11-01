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
	
	boolean isServerStopping = false;

	MessageQueue<ChainReplicationMessage> messageQueue = 
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
	
	public ServerMessageHandler getServerMessageHandler() {
		return serverMessageHandler;
	}





	public void startProcessingMessages() throws ServerChainReplicationException {
	  while(!isServerStopping) {
		  if(messageQueue.hasMoreMessages()) {
			  ChainReplicationMessage message = (ChainReplicationMessage)messageQueue.dequeueMessageAndReturnMessageObject();
			  this.handleMessage(message);
		  }
	  }
	}
	
	public void stopProcessing() {
		this.isServerStopping = true;
	}
	
	public void logMessage(String message) {
		this.serverImpl.logMessage(message);
	}

	public void deliverMessage(ChainReplicationMessage message) {
		if(message != null) {
			messageQueue.enqueueMessageObject(message.getPritority().ordinal(), message);
		}
	}

	public void handleMessage(ChainReplicationMessage message) throws ServerChainReplicationException  {
		if(message.getClass() == RequestMessage.class) {
			this.serverMessageHandler.handleRequestMessage(
					(RequestMessage)message);
		} else if(message.getClass() ==  ResponseOrSyncMessage.class) {
			this.serverMessageHandler.handleSyncMessage((ResponseOrSyncMessage) message);
		} else if(message.getClass() ==  AckMessage.class) {
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
