package async.chainreplication.server.threads;

import java.util.TimerTask;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.ConnectClientException;
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
		try {
			clientHelper.sendMessage(heartBeatMessage);
		} catch (ConnectClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
