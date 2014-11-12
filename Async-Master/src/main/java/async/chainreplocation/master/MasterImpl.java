package async.chainreplocation.master;

import java.util.Timer;

import async.chainreplication.communication.messages.LogMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.threads.HeartBeatCheckerTask;
import async.chainreplication.master.threads.ServerMessageListenerThread;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterImpl.
 */
public class MasterImpl extends ChainReplicationImpl {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		final MasterImpl masterImpl = new MasterImpl(
				ConfigUtil.deserializeFromFile(args[0]), args[1]);
		masterImpl.init();
		// masterImpl.stop();
	}

	/** The master. */
	Master master;

	/** The listerner thread. */
	ServerMessageListenerThread listernerThread;

	/** The checker thread. */
	Timer checkerThread;

	/** The master chain replication facade. */
	MasterChainReplicationFacade masterChainReplicationFacade;

	/** The heart beat timeout. */
	long heartBeatTimeout = 5000;

	/**
	 * Instantiates a new master impl.
	 *
	 * @param config
	 *            the config
	 * @param masterId
	 *            the master id
	 */
	public MasterImpl(Config config, String masterId) {
		super(masterId);
		try {
			master = config.getMaster();
			masterChainReplicationFacade = new MasterChainReplicationFacade(
					master, config.getChains(), config.getClients(), this);
			heartBeatTimeout = master.getHeartbeatTimeout();
		} catch (final Exception e) {
			this.logMessage(e.getMessage());
		}
	}

	/**
	 * Gets the heart beat timeout.
	 *
	 * @return the heart beat timeout
	 */
	public long getHeartBeatTimeout() {
		return heartBeatTimeout;
	}

	/**
	 * Gets the master.
	 *
	 * @return the master
	 */
	public Master getMaster() {
		return master;
	}

	/**
	 * Gets the master chain replication facade.
	 *
	 * @return the master chain replication facade
	 */
	public MasterChainReplicationFacade getMasterChainReplicationFacade() {
		return masterChainReplicationFacade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#init()
	 */
	public void init() {
		try {
			super.init();
			listernerThread = new ServerMessageListenerThread(this);
			checkerThread = new Timer();
			final HeartBeatCheckerTask task = new HeartBeatCheckerTask(this);
			checkerThread.scheduleAtFixedRate(task, 0, heartBeatTimeout);
			listernerThread.start();
			masterChainReplicationFacade.startProcessingMessages();
		} catch (final MasterChainReplicationException e) {
			this.logMessage("Internal Error:" + e.getMessage());
			this.stop();
			e.printStackTrace();
		}
		this.logMessage("Master Started:");
	}

	/**
	 * Log message.
	 *
	 * @param message
	 *            the message
	 */
	public void logMessage(String message) {
		final LogMessage logMessage = new LogMessage(message);
		this.getLogMessages().enqueueMessageObject(
				logMessage.getPriority().ordinal(), logMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#stop()
	 */
	public void stop() {
		this.logMessage("Master Stopping");
		masterChainReplicationFacade.stopProcessingMessages();
		checkerThread.cancel();
		listernerThread.stopThread();
		super.stop();
	}
}
