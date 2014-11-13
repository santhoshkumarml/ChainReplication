package async.chainreplication.server;

import java.util.Timer;

import async.chainreplication.communication.messages.LogMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.chainreplication.server.threads.ChainMessageListenerThread;
import async.chainreplication.server.threads.HeartBeatSenderTask;
import async.chainreplication.server.threads.RequestQueryOrUpdateThread;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerImpl.
 */
public class ServerImpl extends ChainReplicationImpl {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		final Config config = ConfigUtil.deserializeFromFile(args[0]);
		// String command = args[3];
		final String chainName = args[1];
		final String serverId = args[2];
		// assert command.trim().equals("start")||command.trim().equals("stop");
		// if(command.trim().equals("start")) {
		serverImpl = new ServerImpl(config, chainName, serverId);
		serverImpl.init();
		// } else {
		// serverImpl.stop();
		// }
	}

	/** The heart beat time out. */
	long heartBeatTimeOut = 5000;

	/** The is started. */
	boolean isStarted = false;

	/** The heart beat sender timer. */
	Timer heartBeatSenderTimer;

	/** The server impl. */
	private static ServerImpl serverImpl;

	/** The chain message listener thread. */
	ChainMessageListenerThread chainMessageListenerThread;

	/** The request or query update thread. */
	RequestQueryOrUpdateThread requestOrQueryUpdateThread;

	/** The server chain replication facade. */
	ServerChainReplicationFacade serverChainReplicationFacade;

	/**
	 * Instantiates a new server impl.
	 *
	 * @param config
	 *            the config
	 * @param chainName
	 *            the chain name
	 * @param serverId
	 *            the server id
	 */
	public ServerImpl(Config config, String chainName, String serverId) {
		super(chainName + "-" + serverId);
		try {
			serverChainReplicationFacade = new ServerChainReplicationFacade(
					config.getChainToServerMap().get(chainName).get(serverId),
					config.getChains(), config.getMaster(),
					config.getReceiveAndSendMessageLimit().get(
							config.getChainToServerMap().get(chainName).get(serverId)),
							this);
			heartBeatTimeOut = config.getMaster().getHeartbeatTimeout();
		} catch (final ServerChainReplicationException e) {
			this.logMessage(e.getMessage());
		}
		try {
			Thread.sleep(config.getServerToInitialSleepTime().get(
					config.getChainToServerMap().get(chainName).get(serverId)));
		} catch (final InterruptedException e) {
			e.printStackTrace();
			e.printStackTrace();
		}
	}

	/**
	 * Gets the master.
	 *
	 * @return the master
	 */
	public Master getMaster() {
		return serverChainReplicationFacade.getMaster();
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getServer() {
		return serverChainReplicationFacade.getServer();
	}

	/**
	 * Gets the server chain replication facade.
	 *
	 * @return the server chain replication facade
	 */
	public ServerChainReplicationFacade getServerChainReplicationFacade() {
		return serverChainReplicationFacade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#init()
	 */
	public void init() {
		if (!isStarted) {
			super.init();
			this.logMessage("Server Starting" + this.getServer());
			try {
				heartBeatSenderTimer = new Timer();
				final HeartBeatSenderTask heartBeatSender = new HeartBeatSenderTask(
						this);
				heartBeatSenderTimer.scheduleAtFixedRate(heartBeatSender, 0,
						(heartBeatTimeOut - 3000));
				requestOrQueryUpdateThread = new RequestQueryOrUpdateThread(
						this);
				requestOrQueryUpdateThread.start();
				chainMessageListenerThread = new ChainMessageListenerThread(
						this);
				chainMessageListenerThread.start();
				serverChainReplicationFacade.startProcessingMessages();
			} catch (final ServerChainReplicationException e) {
				this.logMessage(e.getMessage());
				this.stop();
				e.printStackTrace();
			}
			isStarted = true;
			this.logMessage("Server started");
		}
	}

	/**
	 * Checks if is head in the chain.
	 *
	 * @return true, if is head in the chain
	 */
	public boolean isHeadInTheChain() {
		return serverChainReplicationFacade.isHeadInTheChain();
	}

	/**
	 * Checks if is tail in the chain.
	 *
	 * @return true, if is tail in the chain
	 */
	public boolean isTailInTheChain() {
		return serverChainReplicationFacade.isTailInTheChain();
	}

	/**
	 * Log message.
	 *
	 * @param message
	 *            the message
	 */
	public synchronized void logMessage(String message) {
		final LogMessage logMessage = new LogMessage(message);
		this.getLogMessages().enqueueMessageObject(
				logMessage.getPriority().ordinal(), logMessage);
	}

	/**
	 * Pause all threads.
	 */
	public void pauseAllThreads() {
		this.logMessage("Pausing message handler threads for update");
		try {
			heartBeatSenderTimer.wait();
			requestOrQueryUpdateThread.wait();
			chainMessageListenerThread.wait();
		} catch (final InterruptedException e) {
			this.logMessage("Internal Error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Resume allthreads.
	 */
	public void resumeAllthreads() {
		this.logMessage("Resuming message handler threads after update");
		heartBeatSenderTimer.notifyAll();
		requestOrQueryUpdateThread.notifyAll();
		chainMessageListenerThread.notifyAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aync.chainreplication.base.impl.ChainReplicationImpl#stop()
	 */
	public void stop() {
		this.logMessage("Server Stopping");
		heartBeatSenderTimer.cancel();
		requestOrQueryUpdateThread.stopThread();
		chainMessageListenerThread.stopThread();
		serverChainReplicationFacade.stopProcessing();
		this.logMessage("Server Stopped");
		super.stop();
		System.exit(0);
	}
}
