package async.chainreplocation.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import async.chainreplication.communication.messages.ChainJoinMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.communication.messages.NewNodeInitializedMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.generic.message.queue.Message;
import async.generic.message.queue.MessageQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterChainReplicationFacade.
 */
public class MasterChainReplicationFacade {

	/** The master impl. */
	MasterImpl masterImpl;

	/** The master message handler. */
	MasterMessageHandler masterMessageHandler;

	/** The message queue. */
	MessageQueue<ChainReplicationMessage> messageQueue = new MessageQueue<ChainReplicationMessage>();

	/** The heart beat message queue. */
	MessageQueue<ChainReplicationMessage> heartBeatMessageQueue = new MessageQueue<ChainReplicationMessage>();


	volatile boolean isMasterStopping = false;

	/**
	 * Instantiates a new master chain replication facade.
	 *
	 * @param master the master
	 * @param chains the chains
	 * @param chainToServerMap the chain to server map
	 * @param clients the clients
	 * @param masterImpl the master impl
	 */
	public MasterChainReplicationFacade(Master master,
			Map<String, Chain> chains,
			Map<String, Client> clients, MasterImpl masterImpl) {
		this.masterImpl = masterImpl;
		masterMessageHandler = new MasterMessageHandler(master, chains, clients, this);
	}

	/**
	 * Deliver message.
	 *
	 * @param message the message
	 */
	public void deliverMessage(ChainReplicationMessage message) {
		//TODO remove this later
		this.logMessages("Message Delivered:"+message.toString());
		if (message != null) {
			if (message.getClass() == HeartBeatMessage.class) {
				synchronized (heartBeatMessageQueue) {
					heartBeatMessageQueue.enqueueMessageObject(message
							.getPriority().ordinal(), message);
				}
			} else {
				messageQueue.enqueueMessageObject(message.getPriority()
						.ordinal(), message);
			}
		}
	}

	/**
	 * Gets the heart beat message queue.
	 *
	 * @return the heart beat message queue
	 */
	public List<Message<ChainReplicationMessage>> dequeueAllHeartBeatMessages() {
		List<Message<ChainReplicationMessage>> messages = new ArrayList<Message<ChainReplicationMessage>>();
		synchronized (heartBeatMessageQueue) {
			while(heartBeatMessageQueue.hasMoreMessages()) {
				messages.add(heartBeatMessageQueue.dequeueMessage());
			}
		}
		return messages;
	}

	public void startProcessingMessages()
			throws MasterChainReplicationException{
		while (!isMasterStopping) {
			if (messageQueue.hasMoreMessages()) {
				ChainReplicationMessage message = (ChainReplicationMessage) messageQueue
						.dequeueMessageAndReturnMessageObject();
				this.handleMessage(message);
			}
		}
	}

	public void stopProcessingMessages() {
		isMasterStopping = true;
	}

	/**
	 * Handle message.
	 *
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public void handleMessage(ChainReplicationMessage message)
			throws MasterChainReplicationException {
		//TODO Remove this later
		this.logMessages("Handling message: "+message.toString());
		if (message.getClass() == MasterGenericServerChangeMessage.class) {
			masterMessageHandler
			.handleGenericServerChangeMessage((MasterGenericServerChangeMessage) message);
		} else if(message.getClass() == ChainJoinMessage.class) {
			masterMessageHandler.handleChainJoinMessage((ChainJoinMessage) message);
		} else if(message.getClass() == NewNodeInitializedMessage.class) {
			masterMessageHandler.handleNewNodeInitializedMessage(
					(NewNodeInitializedMessage) message);
		}
	}

	/**
	 * Log messages.
	 *
	 * @param message the message
	 */
	public void logMessages(String message) {
		masterImpl.logMessage(message);
	}

}
