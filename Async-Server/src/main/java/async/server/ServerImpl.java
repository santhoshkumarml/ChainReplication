package async.server;

import java.util.Timer;

import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.message.facade.ChainReplicationFacade;

public class ServerImpl {	
	long heartBeatTimeOut;
	
	HeartBeatSenderTask heartBeatSender;
	MasterUpdateListenerThread masterUpdateListener;
	ChainReplicationFacade chainReplicationFacade;
	
	public ServerImpl(String serverId, String chainName, String host, int port,
			String masterHost, int masterPort, long heartBeatTimeOut) {
		this.chainReplicationFacade = new ChainReplicationFacade(
				new Server(serverId, chainName, host, port),
				new Master(masterHost, masterPort));
	}
	
	
	
	public ChainReplicationFacade getChainReplicationFacade() {
		return chainReplicationFacade;
	}

	public void initServer() {
		Timer timer = new Timer();
		this.heartBeatSender = new HeartBeatSenderTask(this);
		timer.schedule(heartBeatSender, (heartBeatTimeOut-3000));
		RequestQueryOrUpdateThread requestOrQueryUpdateThread = 
				new RequestQueryOrUpdateThread(this);
		requestOrQueryUpdateThread.start();
	}
}
