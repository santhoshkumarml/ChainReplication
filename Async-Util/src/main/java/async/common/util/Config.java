package async.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

public class Config implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5331163161507115921L;
	Map<String,Chain> chains = new HashMap<String, Chain>();
	Map<String,Map<String,Server>> chainToServerMap = 
			new HashMap<String, Map<String,Server>>();
	Map<String,Client> clients = new HashMap<String, Client>();
	Master master;
	Map<Client, TestCases> testCases = new HashMap<Client, TestCases>();
	
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


	public Map<Client, TestCases> getTestCases() {
		return testCases;
	}

	@Override
	public String toString() {
		return "Config [chains=" + chains + ", chainToServerMap="
				+ chainToServerMap.toString() + ", clients=" + clients.toString() + ", master="
				+ master.toString() + ", testCases=" + testCases.toString() + "]";
	}

}
