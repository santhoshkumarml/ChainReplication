package async.master;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import async.chainreplication.communication.messages.MasterClientChangeMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
import async.chainreplication.communication.messages.MasterServerChangeMessage;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;

public class MasterMessageHandler {
	MasterChainReplicationFacade masterChainReplicationFacade;
	MasterDataStructure masterDs;

	public MasterMessageHandler(Master master, Map<String, Chain> chains,
			Map<String, Map<String, Server>> chainToServerMap,
			Map<String, Client> clients,
			MasterChainReplicationFacade masterChainReplicationFacade) {
		this.masterDs = new MasterDataStructure(chains, master, chainToServerMap, clients);
		this.masterChainReplicationFacade = masterChainReplicationFacade;
	}


	public void handleGenericServerChangeMessage(MasterGenericServerChangeMessage message) {
		Set<Server> diedServers = message.getDiedServers();
		ChainChanges chainChanges = this.masterDs.calculateChanges(diedServers);
		Map<Server, MasterServerChangeMessage> serverMessageChanges = 
				new HashMap<Server, MasterServerChangeMessage>();
		Map<Client, MasterClientChangeMessage> clientMessageChanges = 
				new HashMap<Client, MasterClientChangeMessage>();

		for(String changedChain : chainChanges.getChainsChanged()) {
			String chainId = changedChain;
			Chain chain = this.masterDs.getChains().get(chainId);
		}

		for(Map.Entry<String, Set<String>> changedServersEntry : chainChanges.getChainToServersChanged().entrySet()) {
			String chainId = changedServersEntry.getKey();
			Set<String> serverIdsChanged = changedServersEntry.getValue();
			for(String serverIdChanged : serverIdsChanged) {
				Server server = this.masterDs.getChainToServerMap().get(chainId).get(serverIdChanged);
			}
		}

	}


}
