package async.message.facade;

import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;


public class ChainReplicationFacade {
	ChainReplicationMessageHandler chainReplicationMessageHandler;
	IServerRequestHandler serverRequestHandler;

	public ChainReplicationFacade(Server server, Master master) {
		this.chainReplicationMessageHandler = new ChainReplicationMessageHandler(server, master);
		this.serverRequestHandler = new ServerRequestHandler(chainReplicationMessageHandler); 
	}

}
