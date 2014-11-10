package async.chainreplication.server;

import java.util.Map;

import async.chainreplication.communication.messages.AckMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.communication.messages.ResponseOrSyncMessage;
import async.chainreplication.communication.messages.SuccessorRequestMessage;
import async.chainreplication.communication.messages.WaitServerMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.generic.message.queue.MessageQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerChainReplicationFacade.
 */
public class ServerChainReplicationFacade {

	/** The server message handler. */
	ServerMessageHandler serverMessageHandler;

	/** The is server stopping. */
	boolean isServerStopping = false;

	/** The message queue. */
	MessageQueue<ChainReplicationMessage> messageQueue = new MessageQueue<ChainReplicationMessage>();

	/** The server impl. */
	ServerImpl serverImpl;

	/**
	 * Instantiates a new server chain replication facade.
	 *
	 * @param server the server
	 * @param chainNameToChainMap the chain name to chain map
	 * @param master the master
	 * @param serverImpl the server impl
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
	public ServerChainReplicationFacade(Server server,
			Map<String, Chain> chainNameToChainMap, Master master,
			ServerImpl serverImpl) throws ServerChainReplicationException {
		serverMessageHandler = new ServerMessageHandler(server,
				chainNameToChainMap, master, this);
		this.serverImpl = serverImpl;
	}
	
	
   /**
    * Gets the message queue.
    *
    * @return the message queue
    */
   public MessageQueue<ChainReplicationMessage> getMessageQueue() {
		return messageQueue;
	}


/**
    * ServerImpl.
    *
    * @return {@link ServerImpl}
    */
	public ServerImpl getServerImpl() {
		return serverImpl;
	}

	/**
	 * Deliver message.
	 *
	 * @param message the message
	 */
	public void deliverMessage(ChainReplicationMessage message) {
		if (message != null) {
			messageQueue.enqueueMessageObject(message.getPriority().ordinal(),
					message);
		}
	}

	/**
	 * Gets the master.
	 *
	 * @return the master
	 */
	public Master getMaster() {
		return serverMessageHandler.getMaster();
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getServer() {
		return serverMessageHandler.getServer();
	}

	/**
	 * Gets the server message handler.
	 *
	 * @return the server message handler
	 */
	public ServerMessageHandler getServerMessageHandler() {
		return serverMessageHandler;
	}

	/**
	 * Handle message.
	 *
	 * @param message the message
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
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
		} else if (message.getClass() == WaitServerMessage.class) {
			serverMessageHandler.handleWaitServerMessage((WaitServerMessage)message);
		} else if (message.getClass() == SuccessorRequestMessage.class) {
			serverMessageHandler.handleSuccessorRequestMessage((SuccessorRequestMessage)message);
		}
	}

	/**
	 * Checks if is head in the chain.
	 *
	 * @return true, if is head in the chain
	 */
	public boolean isHeadInTheChain() {
		return serverMessageHandler.getServer().isHead();
	}

	/**
	 * Checks if is tail in the chain.
	 *
	 * @return true, if is tail in the chain
	 */
	public boolean isTailInTheChain() {
		return serverMessageHandler.getServer().isTail();
	}

	/**
	 * Log message.
	 *
	 * @param message the message
	 */
	public void logMessage(String message) {
		serverImpl.logMessage(message);
	}

	/**
	 * Start processing messages.
	 *
	 * @throws ServerChainReplicationException the server chain replication exception
	 */
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

	/**
	 * Stop processing.
	 */
	public void stopProcessing() {
		isServerStopping = true;
	}
}
