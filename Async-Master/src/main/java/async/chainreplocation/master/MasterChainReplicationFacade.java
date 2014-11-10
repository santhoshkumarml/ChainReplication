package async.chainreplocation.master;

import java.util.Map;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
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
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients, MasterImpl masterImpl) {
		this.masterImpl = masterImpl;
		masterMessageHandler = new MasterMessageHandler(master, chains,
				chainToServerMap, clients, this);
	}

	/**
	 * Deliver message.
	 *
	 * @param message the message
	 */
	public void deliverMessage(ChainReplicationMessage message) {
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
	public MessageQueue<ChainReplicationMessage> getHeartBeatMessageQueue() {
		return heartBeatMessageQueue;
	}

	/**
	 * Handle message.
	 *
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public void handleMessage(ChainReplicationMessage message)
			throws MasterChainReplicationException {
		this.logMessages(message.toString());
		if (message.getClass() == MasterGenericServerChangeMessage.class) {
			masterMessageHandler
					.handleGenericServerChangeMessage((MasterGenericServerChangeMessage) message);
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
