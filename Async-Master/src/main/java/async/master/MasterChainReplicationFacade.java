package async.master;

import java.util.Map;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

public class MasterChainReplicationFacade {

	MasterImpl masterImpl;
	MasterMessageHandler masterMessageHandler;

	public MasterChainReplicationFacade(Master master,
			Map<String, Chain> chains,
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients, MasterImpl masterImpl) {
			this.masterImpl = masterImpl;
			this.masterMessageHandler = new MasterMessageHandler(
					master, chains, chainToServerMap,
					clients, this);
	}
	
	public void logMessages(String message) {
		this.masterImpl.logMessage(message);
	}

}
