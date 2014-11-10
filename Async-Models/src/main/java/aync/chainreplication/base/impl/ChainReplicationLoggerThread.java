package aync.chainreplication.base.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

import async.chainreplication.communication.messages.LogMessage;
import async.generic.message.queue.Message;
import async.generic.message.queue.MessageQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainReplicationLoggerThread.
 */
public class ChainReplicationLoggerThread extends Thread {
	
	/** The chain replication impl. */
	ChainReplicationImpl chainReplicationImpl;
	
	/** The chain replicationlogger. */
	Logger chainReplicationlogger;
	
	/** The should still run. */
	volatile boolean shouldStillRun = true;
	/** The pw. */
	PrintWriter pw = null;

	/**
	 * Instantiates a new chain replication logger thread.
	 *
	 * @param chainReplicationImpl the chain replication impl
	 */
	public ChainReplicationLoggerThread(
			ChainReplicationImpl chainReplicationImpl) {
		this.chainReplicationImpl = chainReplicationImpl;
		// chainReplicationlogger =
		// Logger.getLogger(this.chainReplicationImpl.getUniqueId());
		try {
			pw = new PrintWriter(new File("ChainReplication-"
					+ this.chainReplicationImpl.getUniqueId() + ".log"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (shouldStillRun
				|| chainReplicationImpl.getLogMessages().hasMoreMessages()) {
			if (chainReplicationImpl.getLogMessages().hasMoreMessages()) {
				MessageQueue<LogMessage> queue = chainReplicationImpl
						.getLogMessages();
				Message<LogMessage> message = queue.dequeueMessage();
				pw.println(new Date(message.getTimestamp()) + ":"
						+ message.getMessageObject().getMessage() + "\r\n");
				pw.flush();
			}
		}
		if (pw != null) {
			pw.close();
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		shouldStillRun = false;
	}

}
