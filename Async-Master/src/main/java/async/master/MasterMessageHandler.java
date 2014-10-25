package async.master;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import async.connection.util.UDPClientHelper;

public class MasterMessageHandler {
	MasterChainReplicationFacade masterChainReplicationFacade;
	MasterDataStructure masterDs;
	IClientHelper serverMessageHelper,clientMessageHelper = null;

	public MasterMessageHandler(Master master, Map<String, Chain> chains,
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients,
			MasterChainReplicationFacade masterChainReplicationFacade) {
		this.masterDs = new MasterDataStructure(chains, master, chainToServerMap, clients);
		this.masterChainReplicationFacade = masterChainReplicationFacade;
	}

	public void handleGenericServerChangeMessage(MasterGenericServerChangeMessage message)
			throws MasterChainReplicationException {
		Set<Server> diedServers = message.getDiedServers();
		ChainChanges chainChanges = this.masterDs.calculateChanges(diedServers);
		formAndDispatchMessagesForServerAndClient(chainChanges);
	}


	private void formAndDispatchMessagesForServerAndClient(ChainChanges chainChanges) 
			throws MasterChainReplicationException {
		Map<Server, MasterServerChangeMessage> serverMessageChanges = 
				new HashMap<Server, MasterServerChangeMessage>();
		Map<Client, MasterClientChangeMessage> clientMessageChanges = 
				new HashMap<Client, MasterClientChangeMessage>();
		for(Map.Entry<String, Set<String>> changedServersEntry :
			chainChanges.getChainToServersChanged().entrySet()) {
			String chainId = changedServersEntry.getKey();
			Set<String> serverIdsChanged = changedServersEntry.getValue();
			for(String serverIdChanged : serverIdsChanged) {
				Server server = this.masterDs.getChainToServerMap().get(chainId).get(serverIdChanged);
				MasterServerChangeMessage serverMessage = serverMessageChanges.get(server);
				if(serverMessage == null) {
					serverMessage = new MasterServerChangeMessage(server);
				}
				serverMessageChanges.put(server, serverMessage);
			}
		}

		for(String changedChain : chainChanges.getChainsChanged()) {
			String chainId = changedChain;
			Chain chain = this.masterDs.getChains().get(chainId);
			for(Chain allChain : this.masterDs.getChains().values()) {
				if(!allChain.getChainName().equals(chainId)) {
					Server head = allChain.getHead();
					Server tail = allChain.getTail();
					MasterServerChangeMessage serverMessage = serverMessageChanges.get(head);
					if(serverMessage == null) {
						serverMessage = new MasterServerChangeMessage(head);
					}
					serverMessage.getOtherChains().add(chain);
					serverMessageChanges.put(head, serverMessage);
					serverMessage = serverMessageChanges.get(tail);
					if(serverMessage == null) {
						serverMessage = new MasterServerChangeMessage(tail);
					}
					serverMessage.getOtherChains().add(chain);
					serverMessageChanges.put(tail, serverMessage);
				}
			}
			for(Client client : this.masterDs.getClients().values()) {
				MasterClientChangeMessage clientChangeMessage = clientMessageChanges.get(client);
				if(clientChangeMessage == null) {
					clientChangeMessage = new MasterClientChangeMessage(client);
				}
				clientChangeMessage.getChainChanges().add(chain);
				clientMessageChanges.put(client, clientChangeMessage);
			}
		}

		for(Server server : serverMessageChanges.keySet()) {
			MasterServerChangeMessage message = serverMessageChanges.get(server);
			sendServerMessage(server, message);	
		}
		for(Client client : clientMessageChanges.keySet()) {
			MasterClientChangeMessage message = clientMessageChanges.get(client);
			sendClientMessage(client, message);
		}

	}

	private void sendClientMessage(Client client,
			MasterClientChangeMessage message) throws MasterChainReplicationException {
		try {
			clientMessageHelper = new UDPClientHelper(
					client.getClientProcessDetails().getHost(),
					client.getClientProcessDetails().getUdpPort()) ;
			clientMessageHelper.sendMessage(message);
		} catch(ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}
	}


	private void sendServerMessage(Server server, ChainReplicationMessage message)
			throws MasterChainReplicationException {
		try {
			serverMessageHelper = new TCPClientHelper(
					server.getServerProcessDetails().getHost(),
					server.getServerProcessDetails().getTcpPort()) ;
			serverMessageHelper.sendMessage(message);
		} catch(ConnectClientException e) {
			throw new MasterChainReplicationException(e);
		}

	}
}
