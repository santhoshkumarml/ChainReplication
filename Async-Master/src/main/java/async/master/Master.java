package async.master;

import async.chainreplication.master.models.MasterDataStructure;

public class Master {
	MasterDataStructure masterDataStructure = new MasterDataStructure();

	public static void main(String args[]) {
		HeartBeatListenerThread listernerThread = new HeartBeatListenerThread();
		listernerThread.start();
	}
}
