package async.master;

import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Master;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class MasterImpl extends ChainReplicationImpl{

	Master master;
	HeartBeatListenerThread listernerThread;
	MasterChainReplicationFacade masterChainReplicationFacade;

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

	public void logMessage(String message) {
		this.getLogMessages().enqueueMessageObject(message);
	}

	public void init() {
		try {
			super.init();
			listernerThread = new HeartBeatListenerThread(this);
			listernerThread.start();
		} catch(MasterChainReplicationException e) {
			this.logMessage("Internal Error:"+e.getMessage());
		}
		this.logMessage("Master Started:");
	}

	public void stop() {
		this.logMessage("Master Stopping");
		listernerThread.stopThread();
		super.stop();
		this.logMessage("Master Stopped");
	}
}
