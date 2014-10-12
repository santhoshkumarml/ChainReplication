package async.server;

import async.chainreplication.communication.message.models.MasterBankChangeMessage;
import async.connection.message.MessageQueue;

public class MasterUpdateListenerThread {
	
	MessageQueue<MasterBankChangeMessage> masterUpdateQueue = new MessageQueue<MasterBankChangeMessage>();

}
