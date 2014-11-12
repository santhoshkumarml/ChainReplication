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
import async.chainreplication.master.models.Pair;
import async.chainreplication.master.models.Server;
import async.connection.util.ConnectClientException;
import async.connection.util.IClientHelper;
import async.connection.util.TCPClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterMessageHandler.
 */
public class MasterMessageHandler {

	/**
	 * The Class ChainJoinHelper.
	 */
	static class ChainJoinHelper extends Thread {

		/** The master message handler. */
		MasterMessageHandler masterMessageHandler;

		/** The stop running. */
		volatile boolean stopRunning = false;

		/**
		 * Instantiates a new chain join helper.
		 *
		 * @param masterMessageHandler
		 *            the master message handler
		 */
		public ChainJoinHelper(MasterMessageHandler masterMessageHandler) {
			this.masterMessageHandler = masterMessageHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while (!stopRunning) {
				final MasterDataStructure masterDs = masterMessageHandler
						.getMasterDs();
				synchronized (masterDs) {
					final Set<String> chainIds = masterDs
							.getChainToNewServersMap().keySet();
					for (final String chainId : chainIds) {
						final List<Pair<Server, Boolean>> serversToRunning = masterDs
								.getChainToNewServersMap().get(chainId);
						final Pair<Server, Boolean> firstServerToRunning = serversToRunning
								.get(0);
						if (!firstServerToRunning.getSecond()) {
							final MasterChainJoinReplyMessage masterReplyMessage = new MasterChainJoinReplyMessage(
									masterDs.getChains().get(chainId).getTail());
							try {
								masterMessageHandler.sendServerMessage(
										firstServerToRunning.getFirst(),
										masterReplyMessage);
							} catch (final MasterChainReplicationException e) {
								e.printStackTrace();
							}
							firstServerToRunning.setSecond(true);
						}
					}
				}
			}
		}

		/**
		 * Stop server.
		 */
		public void stopServer() {
			stopRunning = true;
		}
	}

	/** The master chain replication facade. */
	MasterChainReplicationFacade masterChainReplicationFacade;

	/** The master ds. */
	MasterDataStructure masterDs;

	/** The client message helper. */
	IClientHelper serverMessageHelper, clientMessageHelper = null;

	/** The chain join helper. */
	ChainJoinHelper chainJoinHelper;

	/**
	 * Instantiates a new master message handler.
	 *
	 * @param master
	 *            the master
	 * @param chains
	 *            the chains
	 * @param clients
	 *            the clients
	 * @param masterChainReplicationFacade
	 *            the master chain replication facade
	 */
	public MasterMessageHandler(Master master, Map<String, Chain> chains,
			Map<String, Client> clients,
			MasterChainReplicationFacade masterChainReplicationFacade) {
		masterDs = new MasterDataStructure(chains, master, clients);
		this.masterChainReplicationFacade = masterChainReplicationFacade;
		chainJoinHelper = new ChainJoinHelper(this);
		chainJoinHelper.start();
	}

	/**
	 * Form and dispatch messages for server and client.
	 *
	 * @param chainChanges
	 *            the chain changes
	 * @throws MasterChainReplicationException
	 *             the master chain replication exception
	 */
	private void formAndDispatchMessagesForServerAndClient(
			ChainChanges chainChanges) throws MasterChainReplicationException {
		final Map<Server, MasterServerChangeMessage> serverMessageChanges = new HashMap<Server, MasterServerChangeMessage>();
		final Map<Client, MasterClientChangeMessage> clientMessageChanges = new HashMap<Client, MasterClientChangeMessage>();
		for (final Map.Entry<String, Set<String>> changedServersEntry : chainChanges
				.getChainToServersChanged().entrySet()) {
			final String chainId = changedServersEntry.getKey();
			final Set<String> serverIdsChanged = changedServersEntry.getValue();
			for (final String serverIdChanged : serverIdsChanged) {
				final Server server = masterDs.getChainToServerMap()
						.get(chainId).get(serverIdChanged);
				MasterServerChangeMessage serverMessage = serverMessageChanges
						.get(server);
				if (serverMessage == null) {
					serverMessage = new MasterServerChangeMessage(server);
				}
				serverMessageChanges.put(server, serverMessage);
			}
		}

		for (final Map.Entry<String, List<Boolean>> changedChainEntry : chainChanges
				.getChainsToHeadTailChanges().entrySet()) {
			final String chainId = changedChainEntry.getKey();
			final Chain chain = masterDs.getChains().get(chainId);
			for (final Chain allChain : masterDs.getChains().values()) {
				if (!allChain.getChainName().equals(chainId)) {
					final Server head = allChain.getHead();
					final Server tail = allChain.getTail();
					MasterServerChangeMessage serverMessage = serverMessageChanges
							.get(head);
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
					if (!changedChainEntry.getValue().get(1)) {
						final List<Pair<Server, Boolean>> serversToRunning = masterDs
								.getChainToNewServersMap().get(chainId);
						if (serversToRunning != null
								&& !serversToRunning.isEmpty()) {
							final Server server = serversToRunning.get(0)
									.getFirst();
							final boolean isServerRunning = serversToRunning
									.get(0).getSecond();
							if (isServerRunning) {
								MasterServerChangeMessage serverMessage = serverMessageChanges
										.get(server);
								if (serverMessage == null) {
									serverMessage = new MasterServerChangeMessage(
											server);
								}
								serverMessage.getOtherChains().add(chain);
								serverMessageChanges.put(server, serverMessage);
							}
						}
					}
				}
			}
			for (final Client client : masterDs.getClients().values()) {
				MasterClientChangeMessage clientChangeMessage = clientMessageChanges
						.get(client);
				if (clientChangeMessage == null) {
					clientChangeMessage = new MasterClientChangeMessage(client);
				}
				clientChangeMessage.getChainChanges().add(chain);
				clientMessageChanges.put(client, clientChangeMessage);
			}
		}

		for (final Server server : serverMessageChanges.keySet()) {
			final MasterServerChangeMessage message = serverMessageChanges
					.get(server);
			sendServerMessage(server, message);
		}
		for (final Client client : clientMessageChanges.keySet()) {
			final MasterClientChangeMessage message = clientMessageChanges
					.get(client);
			sendClientMessage(client, message);
		}

	}

	/**
	 * Gets the chain join helper.
	 *
	 * @return the chain join helper
	 */
	public ChainJoinHelper getChainJoinHelper() {
		return chainJoinHelper;
	}

	/**
	 * Gets the master ds.
	 *
	 * @return the master ds
	 */
	private MasterDataStructure getMasterDs() {
		return masterDs;
	}

	/**
	 * Handle chain join message.
	 *
	 * @param message
	 *            the message
	 * @throws MasterChainReplicationException
	 *             the master chain replication exception
	 */
	public void handleChainJoinMessage(ChainJoinMessage message)
			throws MasterChainReplicationException {
		synchronized (masterDs) {
			final Server server = message.getServer();
			server.getAdjacencyList().setPredecessor(null);
			server.getAdjacencyList().setSucessor(null);
			List<Pair<Server, Boolean>> newServerQueue = masterDs
					.getChainToNewServersMap().get(
							message.getServer().getChainName());
			if (newServerQueue == null) {
				newServerQueue = new LinkedList<Pair<Server, Boolean>>();
			}
			newServerQueue.add(new Pair<Server, Boolean>(server, false));
			masterDs.getChainToNewServersMap().put(server.getChainName(),
					newServerQueue);
		}
	}

	/**
	 * Handle generic server change message.
	 *
	 * @param message
	 *            the message
	 * @throws MasterChainReplicationException
	 *             the master chain replication exception
	 */
	public void handleGenericServerChangeMessage(
			MasterGenericServerChangeMessage message)
			throws MasterChainReplicationException {
		final Set<Server> diedServers = message.getDiedServers();
		final ChainChanges chainChanges = masterDs
				.calculateChanges(diedServers);
		formAndDispatchMessagesForServerAndClient(chainChanges);
	}

	/**
	 * Handle new node initialized message.
	 *
	 * @param message
	 *            the message
	 * @throws MasterChainReplicationException
	 *             the master chain replication exception
	 */
	public void handleNewNodeInitializedMessage(
			NewNodeInitializedMessage message)
			throws MasterChainReplicationException {
		final Server newServer = message.getServer();
		final String chainName = newServer.getChainName();

		synchronized (masterDs) {
			final Chain chain = masterDs.getChains().get(chainName);

			// calculatingChanges
			final ChainChanges changes = new ChainChanges();

			boolean isHeadChanged = false;
			final boolean isTailChanged = true;

			newServer.getAdjacencyList().setSucessor(null);
			newServer.getAdjacencyList().setPredecessor(null);
			final Server exisistingTail = chain.getTail();
			if (exisistingTail != null) {
				exisistingTail.getAdjacencyList().setSucessor(newServer);
				newServer.getAdjacencyList().setPredecessor(exisistingTail);
			}

			if (chain.getHead() == null) {
				isHeadChanged = true;
				chain.setHead(newServer);
			}

			changes.getChainsToHeadTailChanges().put(chainName,
					Arrays.asList(isHeadChanged, isTailChanged));

			Set<String> serversChangedInChain = changes
					.getChainToServersChanged().get(chainName);
			if (serversChangedInChain == null) {
				serversChangedInChain = new HashSet<String>();
			}
			if (exisistingTail != null) {
				serversChangedInChain.add(exisistingTail.getServerId());
			}
			serversChangedInChain.add(newServer.getServerId());

			changes.getChainToServersChanged().put(chainName,
					serversChangedInChain);

			// Master updates
			chain.setTail(newServer);

			masterDs.getChains().put(chainName, chain);
			masterDs.getChainToNewServersMap().get(chainName).remove(0);
			Map<String, Server> servers = masterDs.getChainToServerMap().get(
					chainName);
			if (servers == null) {
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
	 * @param client
	 *            the client
	 * @param message
	 *            the message
	 * @throws MasterChainReplicationException
	 *             the master chain replication exception
	 */
	private void sendClientMessage(Client client,
			MasterClientChangeMessage message)
			throws MasterChainReplicationException {
		// TODO Remove this later
		masterChainReplicationFacade.logMessages(message.toString());
		try {
			clientMessageHelper = new TCPClientHelper(client
					.getClientProcessDetails().getHost(), client
					.getClientProcessDetails().getTcpPort());
			clientMessageHelper.sendMessage(message);
		} catch (final ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}
	}

	/**
	 * Send server message.
	 *
	 * @param server
	 *            the server
	 * @param message
	 *            the message
	 * @throws MasterChainReplicationException
	 *             the master chain replication exception
	 */
	private void sendServerMessage(Server server,
			ChainReplicationMessage message)
			throws MasterChainReplicationException {
		// TODO Remove this later
		masterChainReplicationFacade.logMessages(message.toString());
		try {
			serverMessageHelper = new TCPClientHelper(server
					.getServerProcessDetails().getHost(), server
					.getServerProcessDetails().getTcpPort());
			serverMessageHelper.sendMessage(message);
		} catch (final ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}

	}
}
