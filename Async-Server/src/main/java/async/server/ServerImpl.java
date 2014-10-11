package async.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import async.chainreplication.application.models.Bank;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

public class ServerImpl {	
	Server serverMetadata;
	Master master;
	List<Bank> otherBanks = new ArrayList<Bank>();
	long heartBeatTimeOut;
	
	HeartBeatSenderTask heartBeatSender;
	MasterUpdateListenerThread masterUpdateListener;
	

	public ServerImpl(String serverId, String bankName, String host, int port,
			String masterHost, int masterPort, long heartBeatTimeOut) {
		this.serverMetadata = new Server(serverId, bankName, host, port);
		master = new Master(masterHost, masterPort);
	}
	

	public Server getServerMetadata() {
		return serverMetadata;
	}

	public Master getMaster() {
		return master;
	}

	public List<Bank> getOtherBanks() {
		return otherBanks;
	}
	
	public void initServer() {
		Timer timer = new Timer();
		timer.schedule(heartBeatSender, (heartBeatTimeOut-3000));
	}
}
