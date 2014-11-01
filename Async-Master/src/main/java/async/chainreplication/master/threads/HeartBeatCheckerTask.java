package async.chainreplication.master.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Server;
import async.chainreplocation.master.MasterImpl;
import async.generic.message.queue.Message;
import async.generic.message.queue.MessageQueue;

public class HeartBeatCheckerTask extends TimerTask {
	MasterImpl masterImpl;
	Map<Server, Long> serverToLastHeartBeatSentTime = new HashMap<Server, Long>();  
	public HeartBeatCheckerTask(MasterImpl masterImpl) {
		this.masterImpl = masterImpl;
	}

	@Override
	public void run() {
		List<Server> diedServers = checkAndReturnIfServerDidNotSendHeartBeat();
		if(!diedServers.isEmpty()) {
			ChainReplicationMessage chainReplicationMessage =
					new MasterGenericServerChangeMessage(diedServers);
			try {
				this.masterImpl.getMasterChainReplicationFacade().handleMessage(chainReplicationMessage);
			} catch (MasterChainReplicationException e) {
				this.masterImpl.logMessage("Internal Error:"+e.getMessage());
				e.printStackTrace();
				this.cancel();
			}
		}
	}

	private List<Server> checkAndReturnIfServerDidNotSendHeartBeat() {
		List<Server> diedServers = new ArrayList<Server>();
		synchronized (this.masterImpl.
				getMasterChainReplicationFacade().getHeartBeatMessageQueue()) {
			MessageQueue<ChainReplicationMessage> heartBeatMessageQueue = 
					this.masterImpl.getMasterChainReplicationFacade().getHeartBeatMessageQueue();
			while(heartBeatMessageQueue.hasMoreMessages()) {
				Message<ChainReplicationMessage> message = heartBeatMessageQueue.dequeueMessage();
				HeartBeatMessage heartBeatMessage = (HeartBeatMessage)message.getMessageObject();
				long time = message.getTimestamp();
				this.serverToLastHeartBeatSentTime.put(heartBeatMessage.getServer(), time);
			}
		}
		long currentTime = System.currentTimeMillis();
		for(Entry<Server, Long> serverToHeartBeatTimeStampEntry : this.serverToLastHeartBeatSentTime.entrySet()) {
			long difference = currentTime - serverToHeartBeatTimeStampEntry.getValue();
			if(difference > this.masterImpl.getHeartBeatTimeout()) {
				diedServers.add(serverToHeartBeatTimeStampEntry.getKey());
			}
		}
		return diedServers;
	}
}
