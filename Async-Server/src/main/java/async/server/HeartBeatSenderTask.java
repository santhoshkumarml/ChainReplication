package async.server;

import java.util.TimerTask;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.communication.message.models.HeartBeatMessage;
import async.connection.util.TCPClientHelper;

public class HeartBeatSenderTask extends TimerTask{
	TCPClientHelper clientHelper;
	ServerImpl serverImpl;
	public HeartBeatSenderTask(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		clientHelper = new TCPClientHelper(
				serverImpl.getChainReplicationFacade().getMaster().getMasterHost(),
				serverImpl.getChainReplicationFacade().getMaster().getMasterPort());
	} 

	public void run() {
		ChainReplicationMessage heartBeatMessage = new HeartBeatMessage(
				this.serverImpl.getChainReplicationFacade().getServer());
		clientHelper.sendMessage(heartBeatMessage);
	}
}
