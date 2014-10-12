package async.chainreplication.server.threads;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.server.ServerImpl;
import async.connection.util.IServerStarterHelper;
import async.connection.util.TCPServerStarterHelper;

public class ChainMessageListenerThread extends Thread{
	
	IServerStarterHelper chainMessagesListener;
	ServerImpl serverImpl;
	boolean shouldStillRun = true;
	
	public ChainMessageListenerThread(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
		this.chainMessagesListener = new TCPServerStarterHelper();
	}
	
	public void run()  {
		while(shouldStillRun) {
	       ChainReplicationMessage message = 
	    		   (ChainReplicationMessage)this.chainMessagesListener.acceptAndReadObjectConnection();
	       this.serverImpl.getServerChainReplicationFacade().deliverMessage(message); 
		}
		this.chainMessagesListener.stopServer();
	}
	
	public void stopThread() {
		shouldStillRun = false;
	}
}
