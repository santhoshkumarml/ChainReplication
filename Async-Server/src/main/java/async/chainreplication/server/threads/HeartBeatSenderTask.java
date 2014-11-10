package async.chainreplication.server.threads;

import java.util.TimerTask;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.HeartBeatMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.ConnectClientException;
import async.connection.util.TCPClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class HeartBeatSenderTask.
 */
public class HeartBeatSenderTask extends TimerTask {
	
	/** The client helper. */
	TCPClientHelper clientHelper;
	
	/** The server impl. */
	ServerImpl serverImpl;

	/**
	 * Instantiates a new heart beat sender task.
	 *
	 * @param serverImpl the server impl
	 */
	public HeartBeatSenderTask(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		clientHelper = new TCPClientHelper(serverImpl.getMaster()
				.getMasterHost(), serverImpl.getMaster().getMasterPort());
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public void run() {
		ChainReplicationMessage heartBeatMessage = new HeartBeatMessage(
				serverImpl.getServer());
		try {
			clientHelper.sendMessage(heartBeatMessage);
			// TODO Remove Comment
			this.serverImpl.logMessage(heartBeatMessage.toString());
		} catch (ConnectClientException e) {
			e.printStackTrace();
		}
	}
}
