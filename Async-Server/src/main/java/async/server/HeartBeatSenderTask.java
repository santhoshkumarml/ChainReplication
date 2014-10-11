package async.server;

import java.util.TimerTask;

import async.connection.util.TCPClientHelper;

public class HeartBeatSenderTask extends TimerTask{
	TCPClientHelper clientHelper;
	ServerImpl server;
	public HeartBeatSenderTask(ServerImpl server) {
		this.server = server;
		clientHelper = new TCPClientHelper(
				server.getMaster().getMasterHost(),
				server.getMaster().getMasterPort());
	} 
	
	public void run() {
		clientHelper.sendMessageOverTCPConnection(server.getServerMetadata());
	}
}
