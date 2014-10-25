package async.chainreplication.master.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.master.models.Server;
import async.chainreplocation.master.MasterImpl;

public class HeartBeatCheckerTask extends TimerTask {
	MasterImpl masterImpl;
	
	public HeartBeatCheckerTask(MasterImpl masterImpl) {
		this.masterImpl = masterImpl;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<Server> diedServers = new ArrayList<Server>();
		
		ChainReplicationMessage chainReplicationMessage =
				new MasterGenericServerChangeMessage(diedServers);
		
		

	}
}
