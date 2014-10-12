package async.server;

import async.chainreplication.communication.message.models.MasterOtherChainChangeMessage;
import async.connection.message.MessageQueue;

public class MasterUpdateListenerThread {
	
	MessageQueue<MasterOtherChainChangeMessage> masterUpdateQueue = new MessageQueue<MasterOtherChainChangeMessage>();

}
