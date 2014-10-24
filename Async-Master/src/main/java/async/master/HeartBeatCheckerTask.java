package async.master;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import async.chainreplication.master.models.Server;

public class HeartBeatCheckerTask extends TimerTask {
	MasterImpl masterImpl;
	
	public HeartBeatCheckerTask( MasterImpl masterImpl) {
		this.masterImpl = masterImpl;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<Server> diedServers = new ArrayList<Server>();
		

	}
}
