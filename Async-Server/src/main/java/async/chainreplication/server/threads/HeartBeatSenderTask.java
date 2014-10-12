package async.chainreplication.server.threads;

import java.util.TimerTask;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.communication.message.models.HeartBeatMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.TCPClientHelper;

public class HeartBeatSenderTask extends TimerTask{
	TCPClientHelper clientHelper;
	ServerImpl serverImpl;
	public HeartBeatSenderTask(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		clientHelper = new TCPClientHelper(
				serverImpl.getMaster().getMasterHost(),
				serverImpl.getMaster().getMasterPort());
	} 

	public void run() {
		ChainReplicationMessage heartBeatMessage = new HeartBeatMessage(
				this.serverImpl.getServer());
		clientHelper.sendMessage(heartBeatMessage);
	}
}
