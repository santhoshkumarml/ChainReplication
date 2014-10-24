package async.master;

import java.util.HashMap;
import java.util.Map;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

public class MasterMessageHandler {
	
	Map<String,Chain> chains = new HashMap<String, Chain>();
	Master master;
    Map<String, Map<String,Server>> chainToServerMap = new HashMap<String, Map<String,Server>>();
    Map<String, Client> clients = new HashMap<String, Client>();
    MasterChainReplicationFacade masterChainReplicationFacade;
    
    
	public MasterMessageHandler(Master master, Map<String, Chain> chains,
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients,
			MasterChainReplicationFacade masterChainReplicationFacade) {
		this.chains.putAll(chains);
		this.chainToServerMap.putAll(chainToServerMap);
		this.clients.putAll(clients);
		this.masterChainReplicationFacade = masterChainReplicationFacade;
	}
	
	
}
