package async.message.facade;

import async.chainreplication.communication.message.models.ChainReplicationMessage;
import async.chainreplication.communication.message.models.RequestMessage;
import async.chainreplication.communication.message.models.SyncMessage;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;


public class ChainReplicationFacade {
	ChainReplicationMessageHandler chainReplicationMessageHandler;

	public ChainReplicationFacade(Server server, Master master) {
		this.chainReplicationMessageHandler = 
				new ChainReplicationMessageHandler(server, master);
	}

	public void handleMessage(ChainReplicationMessage message)  {
		if(message instanceof RequestMessage) {
		  	this.chainReplicationMessageHandler.handleRequestMessage(
		  			(RequestMessage)message);
		} else if(message instanceof SyncMessage) {
			this.chainReplicationMessageHandler.handleSyncMessage((SyncMessage) message);
		}
	}
}
