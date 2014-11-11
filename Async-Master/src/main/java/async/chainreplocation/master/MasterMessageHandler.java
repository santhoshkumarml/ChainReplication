package async.chainreplocation.master;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import async.chainreplication.communication.messages.ChainJoinMessage;
import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterClientChangeMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
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
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients,
			MasterChainReplicationFacade masterChainReplicationFacade) {
		masterDs = new MasterDataStructure(chains, master, chainToServerMap,
				clients);
		this.masterChainReplicationFacade = masterChainReplicationFacade;
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
				MasterServerChangeMessage serverMessage = serverMessageChanges
						.get(server);
				if (serverMessage == null) {
					serverMessage = new MasterServerChangeMessage(server);
				}
				serverMessageChanges.put(server, serverMessage);
			}
		}

		for (String changedChain : chainChanges.getChainsChanged()) {
			String chainId = changedChain;
			Chain chain = masterDs.getChains().get(chainId);
			for (Chain allChain : masterDs.getChains().values()) {
				if (!allChain.getChainName().equals(chainId)) {
					Server head = allChain.getHead();
					Server tail = allChain.getTail();
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
			Map<String,Server> serverNameToServerMap = masterDs.getChainToServerMap().get(message.getServer().getChainName());
			
			Server server = message.getServer();
			server.getAdjacencyList().setPredecessor(null);
			server.getAdjacencyList().setSucessor(null);
			Chain chain = masterDs.getChains().get(server.getChainName());
			
			if(serverNameToServerMap!=null && !serverNameToServerMap.isEmpty()) {
				serverNameToServerMap = new HashMap<String, Server>();
				
				chain.setHead(server);
				chain.setTail(server);
				
				serverNameToServerMap.put(server.getServerId(), server);
				masterDs.getChainToServerMap().put(server.getChainName(), serverNameToServerMap);
				masterDs.getChains().put(chain.getChainName(), chain);
			}else {
				Queue<Server> newServerQueue = masterDs.getChainToNewServersMap().get(message.getServer().getChainName());
				if(newServerQueue == null) {
					newServerQueue = new LinkedBlockingQueue<Server>();
				}
				newServerQueue.add(message.getServer());
				//send message to chain tail to propagate 
				//history and application accounts
				sendServerMessage(chain.getTail(), message);
			}
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
		try {
			serverMessageHelper = new TCPClientHelper(server
					.getServerProcessDetails().getHost(), server
					.getServerProcessDetails().getTcpPort());
			serverMessageHelper.sendMessage(message);
		} catch (ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}

	}
}
