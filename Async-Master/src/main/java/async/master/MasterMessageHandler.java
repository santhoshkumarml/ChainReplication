package async.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.MasterGenericServerChangeMessage;
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
		List<Server> diedServers = message.getDiedServers();
		List<ChainReplicationMessage> clientMessages = new ArrayList<ChainReplicationMessage>();
		List<ChainReplicationMessage> groupServerMessages = new ArrayList<ChainReplicationMessage>();
		List<ChainReplicationMessage> otherGroupMessages = new ArrayList<ChainReplicationMessage>();

	}
	
	
}
