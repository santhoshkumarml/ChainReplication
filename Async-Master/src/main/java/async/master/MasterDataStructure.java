package async.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

public class MasterDataStructure {

	Map<String,Chain> chains = new HashMap<String, Chain>();
	Master master;
	Map<String, Map<String,Server>> chainToServerMap = new HashMap<String, Map<String,Server>>();
	Map<String, Client> clients = new HashMap<String, Client>();

	public MasterDataStructure(Map<String, Chain> chains, Master master,
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients) {
		super();
		this.chains = chains;
		this.master = master;
		this.chainToServerMap = chainToServerMap;
		this.clients = clients;
	}
	public Master getMaster() {
		return master;
	}
	public void setMaster(Master master) {
		this.master = master;
	}
	public Map<String, Chain> getChains() {
		return chains;
	}
	public Map<String, Map<String, Server>> getChainToServerMap() {
		return chainToServerMap;
	}
	public Map<String, Client> getClients() {
		return clients;
	}

	public void calculateChanges(List<Server> diedServers) {	
		for(Server diedServer : diedServers) {
			if(diedServer.isHead() || diedServer.isTail()) {
				
			}
		}
	}
}
