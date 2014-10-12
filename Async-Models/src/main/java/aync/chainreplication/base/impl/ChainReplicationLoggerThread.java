package aync.chainreplication.base.impl;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import async.generic.message.queue.MessageQueue;


public class ChainReplicationLoggerThread extends Thread{
	ChainReplicationImpl chainReplicationImpl;
	Logger chainReplicationlogger;
	boolean shouldStillRun = true;
	public ChainReplicationLoggerThread(ChainReplicationImpl chainReplicationImpl) {
		this.chainReplicationImpl = chainReplicationImpl;
		chainReplicationlogger = Logger.getLogger(this.chainReplicationImpl.getUniqueId());
		try {
			FileHandler fh = new FileHandler(
					"E:\\workspace\\Async-Project"+
							this.chainReplicationImpl.getUniqueId()+".log");
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
			chainReplicationlogger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while(shouldStillRun) {
			MessageQueue<String> messageQueue = 
					this.chainReplicationImpl.getLogMessages();
			if(messageQueue.hasMoreMessages()) {
				String message =(String)messageQueue.dequeueMessage();
				chainReplicationlogger.info(message);

			}			
		}
	}

	public void stopThread() {
		shouldStillRun = false;
	}


}
