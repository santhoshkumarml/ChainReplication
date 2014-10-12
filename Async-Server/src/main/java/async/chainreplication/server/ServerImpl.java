package async.chainreplication.server;

import java.util.Timer;

import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.exception.ServerChainReplicationException;
import async.chainreplication.server.threads.ChainMessageListenerThread;
import async.chainreplication.server.threads.HeartBeatSenderTask;
import async.chainreplication.server.threads.MasterUpdateListenerThread;
import async.chainreplication.server.threads.RequestQueryOrUpdateThread;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;

public class ServerImpl extends ChainReplicationImpl{	
	long heartBeatTimeOut;
	Timer heartBeatSenderTimer; 
	MasterUpdateListenerThread masterUpdateListener;
	ChainMessageListenerThread chainMessageListenerThread;
	RequestQueryOrUpdateThread requestOrQueryUpdateThread;
	ServerChainReplicationFacade serverChainReplicationFacade;
	IApplicationRequestHandler appServerRequestHandler;


	public static void main(String args[]) {
		Config config = ConfigUtil.convertToConfig(args[0]);
		String serverId = args[1];
		String chainName = args[2];
		ServerImpl serverImpl = new ServerImpl(config,chainName, serverId);
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
		this.getLogMessages().enqueueMessage(message);
	}


	public void init() {
		super.init();
		heartBeatSenderTimer = new Timer();
		HeartBeatSenderTask heartBeatSender = new HeartBeatSenderTask(this);
		heartBeatSenderTimer.schedule(heartBeatSender, (heartBeatTimeOut-3000));
		requestOrQueryUpdateThread = new RequestQueryOrUpdateThread(this);
		requestOrQueryUpdateThread.start();
		chainMessageListenerThread = new ChainMessageListenerThread(this);
		chainMessageListenerThread.start();
	}

	public void stop() {
		heartBeatSenderTimer.cancel();
		requestOrQueryUpdateThread.stopThread();
		chainMessageListenerThread.stopThread();
		super.stop();
	}

}
