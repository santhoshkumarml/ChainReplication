package async.chainreplication.server;

import java.util.Map;

import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
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

	MessageQueue<ChainReplicationMessage> messageQueue = new MessageQueue<ChainReplicationMessage>();

	ServerImpl serverImpl;

	public ServerChainReplicationFacade(Server server,
			Map<String, Chain> chainNameToChainMap, Master master,
			ServerImpl serverImpl) throws ServerChainReplicationException {
		serverMessageHandler = new ServerMessageHandler(server,
				chainNameToChainMap, master, this);
		this.serverImpl = serverImpl;
	}

	public void deliverMessage(ChainReplicationMessage message) {
		if (message != null) {
			messageQueue.enqueueMessageObject(message.getPriority().ordinal(),
					message);
		}
	}

	public Master getMaster() {
		return serverMessageHandler.getMaster();
	}

	public Server getServer() {
		return serverMessageHandler.getServer();
	}

	public ServerMessageHandler getServerMessageHandler() {
		return serverMessageHandler;
	}

	public void handleMessage(ChainReplicationMessage message)
			throws ServerChainReplicationException {
		if (message.getClass() == RequestMessage.class) {
			serverMessageHandler.handleRequestMessage((RequestMessage) message);
		} else if (message.getClass() == ResponseOrSyncMessage.class) {
			serverMessageHandler
					.handleSyncMessage((ResponseOrSyncMessage) message);
		} else if (message.getClass() == AckMessage.class) {
			serverMessageHandler.handleAckMessage((AckMessage) message);
		} else if (message.getClass() == MasterServerChangeMessage.class) {
			serverMessageHandler
					.handleMasterMessage((MasterServerChangeMessage) message);
		}
	}

	public boolean isHeadInTheChain() {
		return serverMessageHandler.getServer().isHead();
	}

	public boolean isTailInTheChain() {
		return serverMessageHandler.getServer().isTail();
	}

	public void logMessage(String message) {
		serverImpl.logMessage(message);
	}

	public void startProcessingMessages()
			throws ServerChainReplicationException {
		while (!isServerStopping) {
			if (messageQueue.hasMoreMessages()) {
				ChainReplicationMessage message = (ChainReplicationMessage) messageQueue
						.dequeueMessageAndReturnMessageObject();
				this.handleMessage(message);
			}
		}
	}

	public void stopProcessing() {
		isServerStopping = true;
	}
}
