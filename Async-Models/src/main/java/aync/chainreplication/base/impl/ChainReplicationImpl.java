package aync.chainreplication.base.impl;

import async.generic.message.queue.MessageQueue;

public abstract class ChainReplicationImpl {
	MessageQueue<String> logMessages = new MessageQueue<String>();
	String uniqueId;
	ChainReplicationLoggerThread chainReplicationLoggerThread;
	
	public ChainReplicationImpl(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public MessageQueue<String> getLogMessages() {
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
