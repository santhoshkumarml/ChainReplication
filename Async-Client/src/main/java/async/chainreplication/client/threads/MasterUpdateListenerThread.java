package async.chainreplication.client.threads;

import async.chainreplication.client.ClientImpl;


public class MasterUpdateListenerThread extends Thread{
	ClientImpl clientImpl;
	volatile boolean shouldStillRun = true;

	public MasterUpdateListenerThread(ClientImpl clientImpl) {
		this.clientImpl = clientImpl;
	}
	public void run() {
		while(shouldStillRun) {
			
		}
	}
	
	public void stopThread() {
		shouldStillRun = false;
	}

	
}
