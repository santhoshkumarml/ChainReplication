package aync.chainreplication.base.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import async.generic.message.queue.Message;
import async.generic.message.queue.MessageQueue;


public class ChainReplicationLoggerThread extends Thread{
	ChainReplicationImpl chainReplicationImpl;
	Logger chainReplicationlogger;
	volatile boolean shouldStillRun = true;
	FileHandler fh = null;
	PrintWriter pw = null;
	public ChainReplicationLoggerThread(ChainReplicationImpl chainReplicationImpl) {
		this.chainReplicationImpl = chainReplicationImpl;
		//chainReplicationlogger = Logger.getLogger(this.chainReplicationImpl.getUniqueId());
		try {
			pw= new PrintWriter(new File("ChainReplication-"+
							this.chainReplicationImpl.getUniqueId()+".log"));
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while(shouldStillRun || this.chainReplicationImpl.getLogMessages().hasMoreMessages()) {
			if(this.chainReplicationImpl.getLogMessages().hasMoreMessages()) {
				MessageQueue<String> queue =  this.chainReplicationImpl.getLogMessages();
				Message<String> message =(Message<String>)queue.dequeueMessage();
				pw.println(new Date(message.getTimestamp())+":"+message.getMessageObject()+"\r\n");
				pw.flush();
			}			
		}
		if(pw!=null) {
			pw.close();
		}
	}

	public void stopThread() {
		shouldStillRun = false;
	}


}
