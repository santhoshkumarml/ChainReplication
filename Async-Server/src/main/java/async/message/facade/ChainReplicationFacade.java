package async.message.facade;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;


public class ChainReplicationFacade {
	ChainReplicationMessageHandler chainReplicationMessageHandler;

	public ChainReplicationFacade(Server server, Master master) {
		this.chainReplicationMessageHandler = 
				new ChainReplicationMessageHandler(server, master);
	}

	public void handleMessage(ChainReplicationMessage message)  {
      
	}
}
