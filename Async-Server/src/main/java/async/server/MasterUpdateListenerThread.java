package async.server;

import async.chainreplication.master.communication.models.MasterMessage;
import async.connection.message.MessageQueue;

public class MasterUpdateListenerThread {
	
	MessageQueue<MasterMessage> masterUpdateQueue = new MessageQueue<MasterMessage>();

}
