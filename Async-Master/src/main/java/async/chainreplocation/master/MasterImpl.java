package async.chainreplocation.master;

import java.util.Timer;

import async.chainreplication.communication.messages.LogMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.threads.HeartBeatCheckerTask;
import async.chainreplication.master.threads.HeartBeatListenerThread;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class MasterImpl extends ChainReplicationImpl{
	Master master;
	HeartBeatListenerThread listernerThread;
	Timer checkerThread;
	MasterChainReplicationFacade masterChainReplicationFacade;
	long heartBeatTimeout = 5000;

	public static void main(String args[]) {
		MasterImpl masterImpl = new MasterImpl(
				ConfigUtil.deserializeFromFile(args[0]),
				args[1]);
		masterImpl.init();
		masterImpl.stop();
	}

	public MasterImpl(Config config, String masterId) {
		super(masterId);
		try {
			this.master = config.getMaster();
			this.masterChainReplicationFacade = new MasterChainReplicationFacade(
					this.master,
					config.getChains(),
					config.getChainToServerMap(),
					config.getClients(),
					this);
		} catch (Exception e) {
			this.logMessage(e.getMessage());
		}
	}



	public Master getMaster() {
		return master;
	}
	

	public MasterChainReplicationFacade getMasterChainReplicationFacade() {
		return masterChainReplicationFacade;
	}

	public void logMessage(String message) {
		this.getLogMessages().enqueueMessageObject(new LogMessage(message));
	}

	public void init() {
		try {
			super.init();
			listernerThread = new HeartBeatListenerThread(this);
			checkerThread = new Timer();
			HeartBeatCheckerTask task = new HeartBeatCheckerTask(this);
			checkerThread.schedule(task, heartBeatTimeout);
			listernerThread.start();
		} catch(MasterChainReplicationException e) {
			this.logMessage("Internal Error:"+e.getMessage());
			this.stop();
			e.printStackTrace();
		}
		this.logMessage("Master Started:");
	}

	public void stop() {
		this.logMessage("Master Stopping");
		checkerThread.cancel();
		listernerThread.stopThread();
		super.stop();
	}
}
