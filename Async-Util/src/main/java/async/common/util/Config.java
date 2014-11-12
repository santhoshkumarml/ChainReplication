package async.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */
public class Config implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5331163161507115921L;

	/** The chains. */
	Map<String, Chain> chains = new HashMap<String, Chain>();

	/** The chain to server map. */
	Map<String, Map<String, Server>> chainToServerMap = new HashMap<String, Map<String, Server>>();

	/** The clients. */
	Map<String, Client> clients = new HashMap<String, Client>();

	/** The master. */
	Master master;

	/** The test cases. */
	Map<Client, TestCases> testCases = new HashMap<Client, TestCases>();

	/** The server to time to live. */
	Map<Server, Long> serverToTimeToLive = new HashMap<Server, Long>();

	/** The server to initial sleep time. */
	Map<Server, Long> serverToInitialSleepTime = new HashMap<Server, Long>();

	/**
	 * Gets the chains.
	 *
	 * @return the chains
	 */
	public Map<String, Chain> getChains() {
		return chains;
	}

	/**
	 * Gets the chain to server map.
	 *
	 * @return the chain to server map
	 */
	public Map<String, Map<String, Server>> getChainToServerMap() {
		return chainToServerMap;
	}

	/**
	 * Gets the clients.
	 *
	 * @return the clients
	 */
	public Map<String, Client> getClients() {
		return clients;
	}

	/**
	 * Gets the master.
	 *
	 * @return the master
	 */
	public Master getMaster() {
		return master;
	}

	/**
	 * Gets the server to initial sleep time.
	 *
	 * @return the server to initial sleep time
	 */
	public Map<Server, Long> getServerToInitialSleepTime() {
		return serverToInitialSleepTime;
	}

	/**
	 * Gets the server to time to live.
	 *
	 * @return the server to time to live
	 */
	public Map<Server, Long> getServerToTimeToLive() {
		return serverToTimeToLive;
	}

	/**
	 * Gets the test cases.
	 *
	 * @return the test cases
	 */
	public Map<Client, TestCases> getTestCases() {
		return testCases;
	}

	/**
	 * Sets the master.
	 *
	 * @param master
	 *            the new master
	 */
	public void setMaster(Master master) {
		this.master = master;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Config [chains=" + chains + ", chainToServerMap="
				+ chainToServerMap.toString() + ", clients="
				+ clients.toString() + ", master=" + master.toString()
				+ ", testCases=" + testCases.toString() + "]";
	}

}
