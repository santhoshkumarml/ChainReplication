package async.master;

import java.util.Map;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.generic.message.queue.MessageQueue;

public class MasterChainReplicationFacade {
	MasterImpl masterImpl;
	MasterMessageHandler masterMessageHandler;
	
	MessageQueue<ChainReplicationMessage> messageQueue = 
			new MessageQueue<ChainReplicationMessage>();

	public MasterChainReplicationFacade(Master master,
			Map<String, Chain> chains,
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients, MasterImpl masterImpl) {
			this.masterImpl = masterImpl;
			this.masterMessageHandler = new MasterMessageHandler(
					master, chains, chainToServerMap,
					clients, this);
	}
	
	public void logMessages(String message) {
		this.masterImpl.logMessage(message);
	}
	
	public void deliverMessage(ChainReplicationMessage message) {
		if(message != null) {
			messageQueue.enqueueMessageObject(message);
		}
	}

	public void handleMessage(ChainReplicationMessage message) throws MasterChainReplicationException  {
		if(message.getClass() == MasterGenericServerChangeMessage.class) {
			this.masterMessageHandler.handleGenericServerChangeMessage(
					(MasterGenericServerChangeMessage)message);
		} /*else if(message.getClass() ==  ResponseOrSyncMessage.class) {
			this.serverMessageHandler.handleSyncMessage((ResponseOrSyncMessage) message);
		} */
	}

}
