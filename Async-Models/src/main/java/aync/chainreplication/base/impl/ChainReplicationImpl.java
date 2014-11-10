package aync.chainreplication.base.impl;

import async.chainreplication.communication.messages.LogMessage;
import async.generic.message.queue.MessageQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainReplicationImpl.
 */
public abstract class ChainReplicationImpl {
	
	/** The log messages. */
	MessageQueue<LogMessage> logMessages = new MessageQueue<LogMessage>();
	
	/** The unique id. */
	String uniqueId;
	
	/** The chain replication logger thread. */
	ChainReplicationLoggerThread chainReplicationLoggerThread;

	/**
	 * Instantiates a new chain replication impl.
	 *
	 * @param uniqueId the unique id
	 */
	public ChainReplicationImpl(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Gets the log messages.
	 *
	 * @return the log messages
	 */
	public MessageQueue<LogMessage> getLogMessages() {
		return logMessages;
	}

	/**
	 * Gets the unique id.
	 *
	 * @return the unique id
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Inits the.
	 */
	public void init() {
		chainReplicationLoggerThread = new ChainReplicationLoggerThread(this);
		chainReplicationLoggerThread.start();
	}

	/**
	 * Stop.
	 */
	public void stop() {
		chainReplicationLoggerThread.stopThread();
	}
}
