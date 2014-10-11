package async.master;


public class Master {
	MasterDataPersister masterDataStructure = new MasterDataPersister();

	public static void main(String args[]) {
		HeartBeatListenerThread listernerThread = new HeartBeatListenerThread();
		listernerThread.start();
	}
}
