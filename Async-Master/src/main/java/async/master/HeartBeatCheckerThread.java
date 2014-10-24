package async.master;

public class HeartBeatCheckerThread extends Thread {
volatile boolean shouldStillRun = true;
	
	public void run() {
		while(shouldStillRun) {
			
		}
	}
	
	public void stopThread() {
		shouldStillRun = false;
	}
}
