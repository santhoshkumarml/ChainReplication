package async.server;

import async.chainreplication.communication.message.models.MasterMessage;
import async.generic.message.queue.models.MessageQueue;

public class MasterUpdateListenerThread {
	MessageQueue<MasterMessage> masterUpdateQueue = 
			new MessageQueue<MasterMessage>();

	public MessageQueue<MasterMessage> getMasterUpdateQueue() {
		return masterUpdateQueue;
	}
	
}
