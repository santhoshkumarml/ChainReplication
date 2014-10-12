package async.master;

import async.common.util.Config;
import async.common.util.ConfigUtil;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class MasterImpl extends ChainReplicationImpl{

	//MasterDataPersister masterDataStructure = new MasterDataPersister();
	HeartBeatListenerThread listernerThread;

	public static void main(String args[]) {
		MasterImpl masterImpl = new MasterImpl(
				ConfigUtil.convertToConfig(args[0]),
				args[1]);
		masterImpl.init();
		masterImpl.stop();
	}

	public MasterImpl(Config config, String masterId) {
		super(masterId);
	}

	public void init() {
		super.init();
		listernerThread = new HeartBeatListenerThread();
		listernerThread.start();
	}

	public void stop() {
		listernerThread.stopThread();
		super.stop();
	}
}
