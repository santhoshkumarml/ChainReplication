package async.chainreplocation.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Pair;
import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterDataStructure.
 */
public class MasterDataStructure {

	/** The chains. */
	Map<String, Chain> chains = new HashMap<String, Chain>();

	/** The master. */
	Master master;

	/** The chain to server map. */
	Map<String, Map<String, Server>> chainToServerMap = new HashMap<String, Map<String, Server>>();

	/** The clients. */
	Map<String, Client> clients = new HashMap<String, Client>();

	/** The chain to new servers map. */
	Map<String, List<Pair<Server, Boolean>>> chainToNewServersMap = new HashMap<String, List<Pair<Server, Boolean>>>();

	/**
	 * Instantiates a new master data structure.
	 *
	 * @param chains
	 *            the chains
	 * @param master
	 *            the master
	 * @param clients
	 *            the clients
	 */
	public MasterDataStructure(Map<String, Chain> chains, Master master,
			Map<String, Client> clients) {
		this.chains = chains;
		this.master = master;
		this.clients = clients;
	}

	/**
	 * Calculate changes.
	 *
	 * @param diedServers
	 *            the died servers
	 * @return the chain changes
	 */
	public ChainChanges calculateChanges(Set<Server> diedServers) {
		final Map<String, List<Boolean>> chainsToIsHeadChanged = new HashMap<String, List<Boolean>>();
		final Map<String, Set<String>> chainToServersChanged = new HashMap<String, Set<String>>();

		final Map<String, Set<Server>> chainToDiedServers = new HashMap<String, Set<Server>>();
		synchronized (this) {
			for (final Server diedServer : diedServers) {
				final boolean wasPresentinNewNodes = checkAndRemoveFromNewServersChain(diedServer);
				if (!wasPresentinNewNodes) {
					Set<Server> diedServerSet = chainToDiedServers
							.get(diedServer.getChainName());
					if (diedServerSet == null) {
						diedServerSet = new HashSet<Server>();
					}
					diedServerSet.add(diedServer);
					chainToDiedServers.put(diedServer.getChainName(),
							diedServerSet);
				}
			}

			for (final String chainId : chainToDiedServers.keySet()) {
				final Set<Server> diedServerSet = chainToDiedServers
						.get(chainId);
				final Set<String> serverIdsChanged = new HashSet<String>();
				final Chain chain = chains.get(chainId);
				Server temp = chain.getHead();
				final List<Server> servers = new ArrayList<Server>();
				while (temp != null) {
					servers.add(temp);
					temp = temp.getAdjacencyList().getSucessor();
				}
				//Remove died servers
				servers.removeAll(diedServerSet);
				
				
				Server predecessor = null;
				for (int i = 0; i < servers.size(); i++) {
					temp = servers.get(i);
					if ((i == 0 && temp != chains.get(chainId).getHead())
							|| (i == servers.size() - 1 && temp != chains.get(
									chainId).getTail())) {
						List<Boolean> headTailChanges = chainsToIsHeadChanged
								.get(chainId);
						if (headTailChanges == null) {
							headTailChanges = new ArrayList<Boolean>(2);
							headTailChanges.add(false);
							headTailChanges.add(false);
						}
						if (i == 0 && temp != chains.get(chainId).getHead()) {
							headTailChanges.set(0, true);
						}
						if (i == servers.size() - 1
								&& temp != chains.get(chainId).getTail()) {
							headTailChanges.set(1, true);
						}
						chainsToIsHeadChanged.put(chainId, headTailChanges);
						if (i == servers.size() - 1) {
							serverIdsChanged.add(temp.getServerId());
						}
					}
					if (temp.getAdjacencyList().getPredecessor() != predecessor) {
						serverIdsChanged.add(temp.getServerId());
						if (predecessor != null) {
							serverIdsChanged.add(predecessor.getServerId());
							predecessor.getAdjacencyList().setSucessor(temp);
						}
						temp.getAdjacencyList().setPredecessor(predecessor);
					}
					predecessor = temp;
				}
				//set the last guy's successor to be null
				temp.getAdjacencyList().setSucessor(null);
				
				chain.setHead(servers.get(0));
				chain.setTail(servers.get(servers.size() - 1));

				chainToServerMap.get(chainId).clear();
				for (final Server server : servers) {
					chainToServerMap.get(chainId).put(server.getServerId(),
							server);
				}
				chainToServersChanged.put(chainId, serverIdsChanged);
			}
		}
		final ChainChanges chainChanges = new ChainChanges();
		chainChanges.getChainsToHeadTailChanges().putAll(chainsToIsHeadChanged);
		chainChanges.getChainToServersChanged().putAll(chainToServersChanged);
		return chainChanges;
	}

	/**
	 * Check and remove from new servers chain.
	 *
	 * @param diedServer
	 *            the died server
	 * @return true, if successful
	 */
	private boolean checkAndRemoveFromNewServersChain(Server diedServer) {
		final String chainId = diedServer.getChainName();
		final List<Pair<Server, Boolean>> newServers = chainToNewServersMap
				.get(chainId);
		final Iterator<Pair<Server, Boolean>> newServersIterator = newServers
				.iterator();
		while (newServersIterator.hasNext()) {
			final Server server = newServersIterator.next().getFirst();
			if (server.equals(diedServer)) {
				newServersIterator.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the chains.
	 *
	 * @return the chains
	 */
	public Map<String, Chain> getChains() {
		return chains;
	}

	/**
	 * Gets the chain to new servers map.
	 *
	 * @return the chain to new servers map
	 */
	public Map<String, List<Pair<Server, Boolean>>> getChainToNewServersMap() {
		return chainToNewServersMap;
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
	 * Sets the master.
	 *
	 * @param master
	 *            the new master
	 */
	public void setMaster(Master master) {
		this.master = master;
	}
}
