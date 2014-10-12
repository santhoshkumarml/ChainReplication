package async.master;
public class HeartBeatListenerThread extends Thread {
	boolean shouldStillRun = true;
	public void run() {
		while(shouldStillRun) {
			
		}
	}
	
	public void stopThread() {
		shouldStillRun = false;
	}
}
