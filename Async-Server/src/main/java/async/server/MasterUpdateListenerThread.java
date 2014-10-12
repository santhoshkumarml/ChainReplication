package async.server;

import async.chainreplication.communication.message.models.MasterMessage;
import async.connection.message.MessageQueue;

public class MasterUpdateListenerThread {
	
	MessageQueue<MasterMessage> masterUpdateQueue = new MessageQueue<MasterMessage>();

}
