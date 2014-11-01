package aync.chainreplication.base.impl;

import async.chainreplication.communication.messages.LogMessage;
import async.generic.message.queue.MessageQueue;

public abstract class ChainReplicationImpl {
	MessageQueue<LogMessage> logMessages = new MessageQueue<LogMessage>();
	String uniqueId;
	ChainReplicationLoggerThread chainReplicationLoggerThread;
	
	public ChainReplicationImpl(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public MessageQueue<LogMessage> getLogMessages() {
		return logMessages;
	}

	public String getUniqueId() {
		return uniqueId;
	}
   
	public void init(){
		chainReplicationLoggerThread = new ChainReplicationLoggerThread(this);
		chainReplicationLoggerThread.start();
	}
	
	public void stop() {
		chainReplicationLoggerThread.stopThread();
	}
}
