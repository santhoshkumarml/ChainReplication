package async.master;

import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class MasterImpl extends ChainReplicationImpl{

	//MasterDataPersister masterDataStructure = new MasterDataPersister();
	HeartBeatListenerThread listernerThread;

	public static void main(String args[]) {
		MasterImpl masterImpl = new MasterImpl(
				ConfigUtil.deserializeFromFile(args[0]),
				args[1]);
		masterImpl.init();
		masterImpl.stop();
	}

	public MasterImpl(Config config, String masterId) {
		super(masterId);
	}
	
    public void logMessage(String message) {
    	this.getLogMessages().enqueueMessage(message);
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
