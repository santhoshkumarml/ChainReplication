package async.master;


public class MasterImpl {
	MasterDataPersister masterDataStructure = new MasterDataPersister();

	public static void main(String args[]) {
	}
	
	public void initMaster() {
		HeartBeatListenerThread listernerThread = new HeartBeatListenerThread();
		listernerThread.start();
	}
}
