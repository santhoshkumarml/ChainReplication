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
	 * @param masterImpl
	 *            the master impl
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
		final List<Server> diedServers = new ArrayList<Server>();
		synchronized (serverToLastHeartBeatSentTime) {
			for (final Message<ChainReplicationMessage> message : masterImpl
					.getMasterChainReplicationFacade()
					.dequeueAllHeartBeatMessages()) {
				final HeartBeatMessage heartBeatMessage = (HeartBeatMessage) (message
						.getMessageObject());
				final long time = message.getTimestamp();
				serverToLastHeartBeatSentTime.put(heartBeatMessage.getServer(),
						time);
			}
			final long currentTime = System.currentTimeMillis();
			for (final Entry<Server, Long> serverToHeartBeatTimeStampEntry : serverToLastHeartBeatSentTime
					.entrySet()) {
				final long difference = currentTime
						- serverToHeartBeatTimeStampEntry.getValue();
				if (difference > masterImpl.getHeartBeatTimeout()) {
					final Server diedServer = serverToHeartBeatTimeStampEntry
							.getKey();
					diedServers.add(diedServer);
				}
			}
		}
		for (final Server diedServer : diedServers) {
			serverToLastHeartBeatSentTime.remove(diedServer);
		}
		return diedServers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		final List<Server> diedServers = checkAndReturnIfServerDidNotSendHeartBeat();
		if (!diedServers.isEmpty()) {
			final ChainReplicationMessage chainReplicationMessage = new MasterGenericServerChangeMessage(
					diedServers);
			try {
				masterImpl.getMasterChainReplicationFacade().handleMessage(
						chainReplicationMessage);
			} catch (final MasterChainReplicationException e) {
				masterImpl.logMessage("Internal Error:" + e.getMessage());
				e.printStackTrace();
				this.cancel();
			}
		}
	}
}
