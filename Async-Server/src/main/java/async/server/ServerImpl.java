package async.server;

import java.util.ArrayList;
import java.util.List;

import async.chainreplication.master.models.Bank;
import async.chainreplication.master.models.Server;

public class ServerImpl {	
	Server serverMetadata;
	List<Bank> otherBanks = new ArrayList<Bank>();
	
	HeartBeatSenderThread heartBeatSender;
	MasterUpdateListenerThread masterUpdateListener;
	
	public ServerImpl(String serverId, String bankName, String host, int port) {
		this.serverMetadata = new Server(serverId, bankName, host, port);
	}
	
	public void initServer() {
		
	}
}
