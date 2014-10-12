package async.chainreplication.server;

import java.util.Timer;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.threads.ChainMessageListenerThread;
import async.chainreplication.server.threads.HeartBeatSenderTask;
import async.chainreplication.server.threads.MasterUpdateListenerThread;
import async.chainreplication.server.threads.RequestQueryOrUpdateThread;

public class ServerImpl {	
	long heartBeatTimeOut;
	Timer heartBeatSenderTimer; 
	MasterUpdateListenerThread masterUpdateListener;
	ChainMessageListenerThread chainMessageListenerThread;
	RequestQueryOrUpdateThread requestOrQueryUpdateThread;
	ServerChainReplicationFacade serverChainReplicationFacade;
	
	
	public static void main(String args[]) {
		String serverId = args[0];
		String chainName = args[1];
		String host = args[2];
		int port = Integer.parseInt(args[3]);
		String masterHost = args[4];
		int masterPort = Integer.parseInt(args[5]);
		long heartBeatTimeOut = Long.parseLong(args[6]);
		ServerImpl serverImpl = new ServerImpl(
				serverId, chainName, host, port,
				masterHost, masterPort, heartBeatTimeOut);
		serverImpl.initServer();
	}

	public ServerImpl(String serverId, String chainName, String host, int port,
			String masterHost, int masterPort, long heartBeatTimeOut) {
		this.serverChainReplicationFacade = new ServerChainReplicationFacade(
				new Server(serverId, chainName, host, port),
				new Master(masterHost, masterPort));
	}

	public void deliverMessage(ChainReplicationMessage message) {
		this.serverChainReplicationFacade.deliverMessage(message);
	}

	public Server getServer() {
		return this.serverChainReplicationFacade.getServer();
	}

	public Master getMaster() {
		return this.serverChainReplicationFacade.getMaster();
	}

	public boolean isHeadInTheChain() {
		return this.serverChainReplicationFacade.isHeadInTheChain();
	}

	public boolean isTailInTheChain() {
		return this.serverChainReplicationFacade.isTailInTheChain();
	}


	public void initServer() {
		heartBeatSenderTimer = new Timer();
		HeartBeatSenderTask heartBeatSender = new HeartBeatSenderTask(this);
		heartBeatSenderTimer.schedule(heartBeatSender, (heartBeatTimeOut-3000));
		requestOrQueryUpdateThread = new RequestQueryOrUpdateThread(this);
		requestOrQueryUpdateThread.start();
		chainMessageListenerThread = new ChainMessageListenerThread(this);
		chainMessageListenerThread.start();
	}
	
	public void stopServer() {
		heartBeatSenderTimer.cancel();
		requestOrQueryUpdateThread.stopThread();
		chainMessageListenerThread.stopThread();
	}

}
