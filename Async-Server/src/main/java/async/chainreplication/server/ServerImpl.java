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

public class ServerImpl extends ChainReplicationImpl{	
	long heartBeatTimeOut = 5000;
	Timer heartBeatSenderTimer; 
	ChainMessageListenerThread chainMessageListenerThread;
	RequestQueryOrUpdateThread requestOrQueryUpdateThread;
	ServerChainReplicationFacade serverChainReplicationFacade;


	public static void main(String args[]) {
		Config config = ConfigUtil.deserializeFromFile(args[0]);
		String chainName = args[1];
		String serverId = args[2];
		ServerImpl serverImpl = new ServerImpl(config, chainName, serverId);
		serverImpl.init();
	}

	public ServerImpl(Config config,String chainName, String serverId) {
		super(chainName+"-"+serverId);
		try {
			this.serverChainReplicationFacade = new ServerChainReplicationFacade(
					config.getChainToServerMap().get(chainName).get(serverId),
					config.getChains(),
					config.getMaster(),
					this);
		} catch (ServerChainReplicationException e) {
			this.logMessage(e.getMessage());
		}
	}

	public ServerChainReplicationFacade getServerChainReplicationFacade() {
		return serverChainReplicationFacade;
	}

	public Server getServer() {
		return this.serverChainReplicationFacade.getServer();
	}

	public Master getMaster() {
		return this.serverChainReplicationFacade.getMaster();
	}

	public boolean isHeadInTheChain() {
		return this.serverChainReplicationFacade.isHeadInTheChain();
	}

	public boolean isTailInTheChain() {
		return this.serverChainReplicationFacade.isTailInTheChain();
	}

	public void logMessage(String message) {
		this.getLogMessages().enqueueMessageObject(new LogMessage(message));
	}


	public void init() {
		super.init();
		this.logMessage("Server Starting"+this.getServer());
		try {
			heartBeatSenderTimer = new Timer();
			HeartBeatSenderTask heartBeatSender = new HeartBeatSenderTask(this);
			heartBeatSenderTimer.schedule(heartBeatSender, (heartBeatTimeOut-3000));
			requestOrQueryUpdateThread = new RequestQueryOrUpdateThread(this);
			requestOrQueryUpdateThread.start();
			chainMessageListenerThread = new ChainMessageListenerThread(this);
			chainMessageListenerThread.start();
			this.serverChainReplicationFacade.startProcessingMessages();
		} catch (ServerChainReplicationException e) {
			this.logMessage(e.getMessage());
			this.stop();
			e.printStackTrace();
		}
		this.logMessage("Server started");
	}

	public void stop() {
		this.logMessage("Server Stopping");
		heartBeatSenderTimer.cancel();
		requestOrQueryUpdateThread.stopThread();
		chainMessageListenerThread.stopThread();
		this.serverChainReplicationFacade.stopProcessing();
		super.stop();
		this.logMessage("Server Stopped");
	}
	
	public void pauseAllThreads() {
		this.logMessage("Pausing message handler threads for update");
		try {
			heartBeatSenderTimer.wait();
			requestOrQueryUpdateThread.wait();
			chainMessageListenerThread.wait();
		} catch (InterruptedException e) {
			this.logMessage("Internal Error:"+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void resumeAllthreads() {
		this.logMessage("Resuming message handler threads after update");
		heartBeatSenderTimer.notifyAll();
		requestOrQueryUpdateThread.notifyAll();
		chainMessageListenerThread.notifyAll();
	}
}
