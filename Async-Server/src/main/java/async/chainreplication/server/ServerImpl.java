package async.chainreplication.server;

import java.util.Timer;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.threads.ChainMessageListenerThread;
import async.chainreplication.server.threads.HeartBeatSenderTask;
import async.chainreplication.server.threads.MasterUpdateListenerThread;
import async.chainreplication.server.threads.RequestQueryOrUpdateThread;

public class ServerImpl {	
	long heartBeatTimeOut;

	HeartBeatSenderTask heartBeatSender;
	MasterUpdateListenerThread masterUpdateListener;
	ChainMessageListenerThread chainMessageListenerThread;
	RequestQueryOrUpdateThread requestOrQueryUpdateThread;
	ChainReplicationFacade chainReplicationFacade;

	public ServerImpl(String serverId, String chainName, String host, int port,
			String masterHost, int masterPort, long heartBeatTimeOut) {
		this.chainReplicationFacade = new ChainReplicationFacade(
				new Server(serverId, chainName, host, port),
				new Master(masterHost, masterPort));
	}

	public void deliverMessage(ChainReplicationMessage message) {
		this.chainReplicationFacade.deliverMessage(message);
	}

	public Server getServer() {
		return this.chainReplicationFacade.getServer();
	}

	public Master getMaster() {
		return this.chainReplicationFacade.getMaster();
	}

	public boolean isHeadInTheChain() {
		return this.chainReplicationFacade.isHeadInTheChain();
	}

	public boolean isTailInTheChain() {
		return this.chainReplicationFacade.isTailInTheChain();
	}


	public void initServer() {
		Timer timer = new Timer();
		this.heartBeatSender = new HeartBeatSenderTask(this);
		timer.schedule(heartBeatSender, (heartBeatTimeOut-3000));
		requestOrQueryUpdateThread = new RequestQueryOrUpdateThread(this);
		requestOrQueryUpdateThread.start();
		chainMessageListenerThread = new ChainMessageListenerThread(this);
		chainMessageListenerThread.start();
	}

}
