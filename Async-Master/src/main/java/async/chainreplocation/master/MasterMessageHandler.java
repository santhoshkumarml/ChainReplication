package async.chainreplocation.master;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import async.chainreplication.communication.messages.ChainJoinMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterChainJoinReplyMessage;
import async.chainreplication.communication.messages.MasterClientChangeMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
import async.chainreplication.communication.messages.NewNodeInitializedMessage;
import async.chainreplication.master.exception.MasterChainReplicationException;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.connection.util.ConnectClientException;
import async.connection.util.IClientHelper;
import async.connection.util.TCPClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterMessageHandler.
 */
public class MasterMessageHandler {

	/** The master chain replication facade. */
	MasterChainReplicationFacade masterChainReplicationFacade;

	/** The master ds. */
	MasterDataStructure masterDs;

	/** The client message helper. */
	IClientHelper serverMessageHelper, clientMessageHelper = null;


	ChainJoinHelper chainJoinHelper;

	/**
	 * Instantiates a new master message handler.
	 *
	 * @param master the master
	 * @param chains the chains
	 * @param chainToServerMap the chain to server map
	 * @param clients the clients
	 * @param masterChainReplicationFacade the master chain replication facade
	 */
	public MasterMessageHandler(Master master, Map<String, Chain> chains,
			Map<String, Client> clients,
			MasterChainReplicationFacade masterChainReplicationFacade) {
		masterDs = new MasterDataStructure(chains, master,clients);
		this.masterChainReplicationFacade = masterChainReplicationFacade;
		this.chainJoinHelper = new ChainJoinHelper(this);
		this.chainJoinHelper.start();
	}



	private MasterDataStructure getMasterDs() {
		return masterDs;
	}



	/**
	 * Form and dispatch messages for server and client.
	 *
	 * @param chainChanges the chain changes
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	private void formAndDispatchMessagesForServerAndClient(
			ChainChanges chainChanges) throws MasterChainReplicationException {
		Map<Server, MasterServerChangeMessage> serverMessageChanges = new HashMap<Server, MasterServerChangeMessage>();
		Map<Client, MasterClientChangeMessage> clientMessageChanges = new HashMap<Client, MasterClientChangeMessage>();
		for (Map.Entry<String, Set<String>> changedServersEntry : chainChanges
				.getChainToServersChanged().entrySet()) {
			String chainId = changedServersEntry.getKey();
			Set<String> serverIdsChanged = changedServersEntry.getValue();
			for (String serverIdChanged : serverIdsChanged) {
				Server server = masterDs.getChainToServerMap().get(chainId)
						.get(serverIdChanged);
				MasterServerChangeMessage serverMessage = serverMessageChanges.get(server);
				if (serverMessage == null) {
					serverMessage = new MasterServerChangeMessage(server);
				}
				serverMessageChanges.put(server, serverMessage);
			}
		}

		for (Map.Entry<String, List<Boolean>> changedChainEntry
				: chainChanges.getChainsToHeadTailChanges().entrySet()) {
			String chainId = changedChainEntry.getKey();
			Chain chain = masterDs.getChains().get(chainId);
			for (Chain allChain : masterDs.getChains().values()) {
				if (!allChain.getChainName().equals(chainId)) {
					Server head = allChain.getHead();
					Server tail = allChain.getTail();
					MasterServerChangeMessage serverMessage = serverMessageChanges.get(head);
					if (serverMessage == null) {
						serverMessage = new MasterServerChangeMessage(head);
					}
					serverMessage.getOtherChains().add(chain);
					serverMessageChanges.put(head, serverMessage);
					serverMessage = serverMessageChanges.get(tail);
					if (serverMessage == null) {
						serverMessage = new MasterServerChangeMessage(tail);
					}
					serverMessage.getOtherChains().add(chain);
					serverMessageChanges.put(tail, serverMessage);
				} else {
					if(!changedChainEntry.getValue().get(1)) {
						List<Pair<Server,Boolean>> serversToRunning = masterDs.getChainToNewServersMap().get(chainId);
						if(serversToRunning!=null && !serversToRunning.isEmpty()) {
							Server server = serversToRunning.get(0).getFirst();
							boolean isServerRunning = serversToRunning.get(0).getSecond();
							if(isServerRunning) {
								MasterServerChangeMessage serverMessage = serverMessageChanges.get(server);
								if (serverMessage == null) {
									serverMessage = new MasterServerChangeMessage(server);
								}
								serverMessage.getOtherChains().add(chain);
								serverMessageChanges.put(server, serverMessage);
							}
						}
					}
				}
			}
			for (Client client : masterDs.getClients().values()) {
				MasterClientChangeMessage clientChangeMessage = clientMessageChanges
						.get(client);
				if (clientChangeMessage == null) {
					clientChangeMessage = new MasterClientChangeMessage(client);
				}
				clientChangeMessage.getChainChanges().add(chain);
				clientMessageChanges.put(client, clientChangeMessage);
			}
		}

		for (Server server : serverMessageChanges.keySet()) {
			MasterServerChangeMessage message = serverMessageChanges
					.get(server);
			sendServerMessage(server, message);
		}
		for (Client client : clientMessageChanges.keySet()) {
			MasterClientChangeMessage message = clientMessageChanges
					.get(client);
			sendClientMessage(client, message);
		}

	}

	/**
	 * Handle generic server change message.
	 *
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public void handleGenericServerChangeMessage(
			MasterGenericServerChangeMessage message)
					throws MasterChainReplicationException {
		Set<Server> diedServers = message.getDiedServers();
		ChainChanges chainChanges = masterDs.calculateChanges(diedServers);
		formAndDispatchMessagesForServerAndClient(chainChanges);
	}


	/**
	 * Handle chain join message.
	 *
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public void handleChainJoinMessage(ChainJoinMessage message) throws MasterChainReplicationException {
		synchronized (masterDs) {
			Server server = message.getServer();
			server.getAdjacencyList().setPredecessor(null);
			server.getAdjacencyList().setSucessor(null);
			List<Pair<Server,Boolean>> newServerQueue = masterDs.getChainToNewServersMap().get(message.getServer().getChainName());
			if(newServerQueue == null) {
				newServerQueue = new LinkedList<Pair<Server,Boolean>>();
			}
			newServerQueue.add(new Pair<Server, Boolean>(server, false));
			masterDs.getChainToNewServersMap().put(server.getChainName(), newServerQueue);
		}
	}


	/**
	 * Handle new node initialized message.
	 *
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	public void handleNewNodeInitializedMessage(NewNodeInitializedMessage message) throws MasterChainReplicationException {
		Server newServer = message.getServer();
		String chainName = newServer.getChainName();

		synchronized (masterDs) {
			Chain chain = masterDs.getChains().get(chainName);

			//calculatingChanges
			ChainChanges changes = new ChainChanges();

			boolean isHeadChanged = false;
			boolean isTailChanged = true;

			newServer.getAdjacencyList().setSucessor(null);
			newServer.getAdjacencyList().setPredecessor(null);
			Server exisistingTail = chain.getTail();
			if(exisistingTail != null) {
				exisistingTail.getAdjacencyList().setSucessor(newServer);
				newServer.getAdjacencyList().setPredecessor(exisistingTail);
			}

			if(chain.getHead() == null) {
				isHeadChanged=true;
				chain.setHead(newServer);
			}

			changes.getChainsToHeadTailChanges().put(chainName,
					Arrays.asList(isHeadChanged,isTailChanged));

			Set<String> serversChangedInChain =
					changes.getChainToServersChanged().get(chainName);
			if(serversChangedInChain == null) {
				serversChangedInChain = new HashSet<String>();
			}
			if(exisistingTail!=null) {
				serversChangedInChain.add(exisistingTail.getServerId());
			}
			serversChangedInChain.add(newServer.getServerId());

			changes.getChainToServersChanged().put(chainName, serversChangedInChain);

			//Master updates
			chain.setTail(newServer);

			masterDs.getChains().put(chainName, chain);
			masterDs.getChainToNewServersMap().get(chainName).remove(0);
			Map<String, Server> servers = masterDs.getChainToServerMap().get(chainName);
			if(servers == null) {
				servers = new HashMap<String, Server>();
			}
			servers.put(exisistingTail.getServerId(), exisistingTail);
			servers.put(newServer.getServerId(), newServer);
			masterDs.getChainToServerMap().put(chainName, servers);
			formAndDispatchMessagesForServerAndClient(changes);
		}
	}


	/**
	 * Send client message.
	 *
	 * @param client the client
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	private void sendClientMessage(Client client,
			MasterClientChangeMessage message)
					throws MasterChainReplicationException {
		//TODO Remove this later
		this.masterChainReplicationFacade.logMessages(message.toString());
		try {
			clientMessageHelper = new TCPClientHelper(client
					.getClientProcessDetails().getHost(), client
					.getClientProcessDetails().getTcpPort());
			clientMessageHelper.sendMessage(message);
		} catch (ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}
	}

	/**
	 * Send server message.
	 *
	 * @param server the server
	 * @param message the message
	 * @throws MasterChainReplicationException the master chain replication exception
	 */
	private void sendServerMessage(Server server,
			ChainReplicationMessage message)
					throws MasterChainReplicationException {
		//TODO Remove this later
		this.masterChainReplicationFacade.logMessages(message.toString());
		try {
			serverMessageHelper = new TCPClientHelper(server
					.getServerProcessDetails().getHost(), server
					.getServerProcessDetails().getTcpPort());
			serverMessageHelper.sendMessage(message);
		} catch (ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}

	}

	private static class ChainJoinHelper extends Thread {
		MasterMessageHandler masterMessageHandler;
		public ChainJoinHelper(MasterMessageHandler masterMessageHandler) {
			this.masterMessageHandler = masterMessageHandler;
		}

		public void run() {
			MasterDataStructure masterDs = masterMessageHandler.getMasterDs();
			synchronized (masterDs) {
				Set<String> chainIds = masterDs.getChainToNewServersMap().keySet();
				for(String chainId: chainIds) {
					List<Pair<Server, Boolean>> serversToRunning = 
							masterDs.getChainToNewServersMap().get(chainId);
					Pair<Server, Boolean> firstServerToRunning = serversToRunning.get(0);
					if(!firstServerToRunning.getSecond()) {
						MasterChainJoinReplyMessage masterReplyMessage = new MasterChainJoinReplyMessage(masterDs.getChains().get(chainId).getTail());
						try {
							this.masterMessageHandler.sendServerMessage(
									firstServerToRunning.getFirst(),
									masterReplyMessage);
						} catch (MasterChainReplicationException e) {
							e.printStackTrace();
						}
						firstServerToRunning.setSecond(true);
					}
				}
			}
		}
	}
}
