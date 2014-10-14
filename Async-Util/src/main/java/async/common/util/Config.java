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


/*	public static Config createDefaultValues() {
		Config config = new Config();
		Client client1 = new Client("client1", "localhost", 10001);
		client1.getClientProcessDetails().setTcpPort(10011);
		client1.getClientProcessDetails().setUdpPort(10021);
		
		Client client2 = new Client("client2", "localhost", 10002);
		client2.getClientProcessDetails().setTcpPort(10012);
		client2.getClientProcessDetails().setUdpPort(10022);
		
		Client client3 = new Client("client3", "localhost", 10003);	
		client3.getClientProcessDetails().setTcpPort(10013);
		client3.getClientProcessDetails().setUdpPort(10023);
		
		
		Server server11 = new Server(String.valueOf(11), "chain1", "localhost", 10101);
		server11.getServerProcessDetails().setTcpPort(10111);
		server11.getServerProcessDetails().setUdpPort(10121);
		Server server12 = new Server(String.valueOf(12), "chain1", "localhost", 10102);
		server12.getServerProcessDetails().setTcpPort(10112);
		server12.getServerProcessDetails().setUdpPort(10122);
		Server server13 = new Server(String.valueOf(13), "chain1", "localhost", 10103);
		server13.getServerProcessDetails().setTcpPort(10113);
		server13.getServerProcessDetails().setUdpPort(10123);
		
		server11.getAdjacencyList().setPredecessor(null);
		server11.getAdjacencyList().setSucessor(server12);
		server12.getAdjacencyList().setPredecessor(server11);
		server12.getAdjacencyList().setSucessor(server13);
		server13.getAdjacencyList().setPredecessor(server12);
		server13.getAdjacencyList().setSucessor(null);
		
		Chain chain1 = new Chain("chain1", server11, server13);
		
		
		Server server21 = new Server(String.valueOf(21), "chain2", "localhost", 10201);
		server21.getServerProcessDetails().setTcpPort(10211);
		server21.getServerProcessDetails().setUdpPort(10221);
		Server server22 = new Server(String.valueOf(22), "chain2", "localhost", 10202);
		server22.getServerProcessDetails().setTcpPort(10212);
		server22.getServerProcessDetails().setUdpPort(10222);
		Server server23 = new Server(String.valueOf(23), "chain2", "localhost", 10203);
		server23.getServerProcessDetails().setTcpPort(10213);
		server23.getServerProcessDetails().setUdpPort(10223);
		
		server21.getAdjacencyList().setPredecessor(null);
		server21.getAdjacencyList().setSucessor(server22);
		server22.getAdjacencyList().setPredecessor(server21);
		server22.getAdjacencyList().setSucessor(server23);
		server23.getAdjacencyList().setPredecessor(server22);
		server23.getAdjacencyList().setSucessor(null);
		
		Chain chain2 = new Chain("chain2", server21, server23);
		
		
		config.getChains().put(chain1.getChainName(), chain1);
		
		Server temp = chain1.getHead();
		Map<String,Server> serverIdToServerMap1 =  new HashMap<String, Server>();
		while(temp != null) {
			serverIdToServerMap1.put(temp.getServerId(), temp);
			temp = temp.getAdjacencyList().getSucessor();
		}
		config.getChainToServerMap().put(chain1.getChainName(), serverIdToServerMap1);
		
		
		/*config.getChains().put(chain2.getChainName(), chain2);
		temp = chain2.getHead();
		Map<String,Server> serverIdToServerMap2 =  new HashMap<String, Server>();
		while(temp != null) {
			serverIdToServerMap2.put(temp.getServerId(), temp);
			temp = temp.getAdjacencyList().getSucessor();
		}
		config.getChainToServerMap().put(chain2.getChainName(), serverIdToServerMap2);
		
		config.getClients().put(client1.getClientId(), client1);
		config.getClients().put(client2.getClientId(), client2);
		//config.getClients().put(client3.getClientId(), client3);
		
		Master master = new Master("localhost",7300, "master");
		config.setMaster(master);
		
		return config;
	}*/


	@Override
	public String toString() {
		return "Config [chains=" + chains + ", chainToServerMap="
				+ chainToServerMap.toString() + ", clients=" + clients.toString() + ", master="
				+ master.toString() + ", testCases=" + testCases.toString() + "]";
	}
	
	

}
