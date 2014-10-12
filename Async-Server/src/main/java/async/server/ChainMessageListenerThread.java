package async.server;

import async.connection.util.IServerStarterHelper;

public class ChainMessageListenerThread {
	
	IServerStarterHelper chainMessagesListener;
	ServerImpl serverImpl;
	
	public ChainMessageListenerThread(ServerImpl serverImpl) {
		this.serverImpl = serverImpl;
	}
	
	public void run()  {
		
	}
	

}
