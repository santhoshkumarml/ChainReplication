package async.master;

import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class MasterImpl extends ChainReplicationImpl{

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
			this.masterChainReplicationFacade = new MasterChainReplicationFacade(
					config.getMaster(),
					config.getChains(),
					config.getChainToServerMap(),
					config.getClients(),
					this);
		} catch (Exception e) {
			this.logMessage(e.getMessage());
		}
	}
	
    public void logMessage(String message) {
    	this.getLogMessages().enqueueMessageObject(message);
    }

	public void init() {
		super.init();
		listernerThread = new HeartBeatListenerThread();
		listernerThread.start();
		this.logMessage("Master Started:");
	}

	public void stop() {
		listernerThread.stopThread();
		super.stop();
	}
}
