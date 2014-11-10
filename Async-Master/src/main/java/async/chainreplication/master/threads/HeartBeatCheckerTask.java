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

// TODO: Auto-generated Javadoc
/**
 * The Class HeartBeatCheckerTask.
 */
public class HeartBeatCheckerTask extends TimerTask {

	/** The master impl. */
	MasterImpl masterImpl;

	/** The server to last heart beat sent time. */
	Map<Server, Long> serverToLastHeartBeatSentTime = new HashMap<Server, Long>();

	/**
	 * Instantiates a new heart beat checker task.
	 *
	 * @param masterImpl the master impl
	 */
	public HeartBeatCheckerTask(MasterImpl masterImpl) {
		this.masterImpl = masterImpl;
	}

	/**
	 * Check and return if server did not send heart beat.
	 *
	 * @return the list
	 */
	private List<Server> checkAndReturnIfServerDidNotSendHeartBeat() {
		List<Server> diedServers = new ArrayList<Server>();
		synchronized (serverToLastHeartBeatSentTime) {
			for (Message<ChainReplicationMessage> message:
				this.masterImpl.getMasterChainReplicationFacade().dequeueAllHeartBeatMessages()) {
				HeartBeatMessage heartBeatMessage = (HeartBeatMessage) (message.getMessageObject());
				long time = message.getTimestamp();
				serverToLastHeartBeatSentTime.put(heartBeatMessage.getServer(),
						time);
			}
			long currentTime = System.currentTimeMillis();
			for (Entry<Server, Long> serverToHeartBeatTimeStampEntry : serverToLastHeartBeatSentTime
					.entrySet()) {
				long difference = currentTime
						- serverToHeartBeatTimeStampEntry.getValue();
				if (difference > masterImpl.getHeartBeatTimeout()) {
					Server diedServer = serverToHeartBeatTimeStampEntry.getKey();
					diedServers.add(diedServer);
				}
			}
		}
		for(Server diedServer : diedServers) {
			this.serverToLastHeartBeatSentTime.remove(diedServer);	
		}
		return diedServers;
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		List<Server> diedServers = checkAndReturnIfServerDidNotSendHeartBeat();
		if (!diedServers.isEmpty()) {
			ChainReplicationMessage chainReplicationMessage = new MasterGenericServerChangeMessage(
					diedServers);
			try {
				masterImpl.getMasterChainReplicationFacade().handleMessage(
						chainReplicationMessage);
			} catch (MasterChainReplicationException e) {
				masterImpl.logMessage("Internal Error:" + e.getMessage());
				e.printStackTrace();
				this.cancel();
			}
		}
	}
}
