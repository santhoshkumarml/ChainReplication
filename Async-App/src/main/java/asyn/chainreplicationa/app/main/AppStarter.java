package asyn.chainreplicationa.app.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import async.chainreplication.client.ClientImpl;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.ServerImpl;
import async.common.app.config.util.AppConfigUtil;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import async.master.MasterImpl;

public class AppStarter {
	public static void main(String[] args) {
		String configFile = args[0];
		try{
			Config config = AppConfigUtil.readConfigFromFile(configFile);
			createMaster(config);
			createServersForChains(config);
			createClients(config);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static List<ProcessBuilder> createServersForChains(
			Config config) {
		//Map<String,Map<String, Server>>config.getChainToServerMap()
		List<ProcessBuilder> serverProcesses = new ArrayList<ProcessBuilder>();
		for(String chainId : config.getChains().keySet()) {
			for(String serverId : config.getChainToServerMap().get(chainId).keySet()) {
				Server server =  config.getChainToServerMap().get(chainId).get(serverId);
				ProcessBuilder pb = createProcessForServer(config, server);
				serverProcesses.add(pb);
			}
		}
		return serverProcesses;

	}

	private static ProcessBuilder createProcessForServer(
			Config config, Server server ) {
		Map<String,String> envs = System.getenv();
		ProcessBuilder pb = new ProcessBuilder(
				"java",
				ServerImpl.class.getName(),
				new String(ConfigUtil.convertToBytes(config)),
				server.getBankName(),
				server.getServerId());
		pb.environment().putAll(envs);
		return pb;
	}


	private static List<ProcessBuilder> createClients(
			Config config) {
		List<ProcessBuilder> clientProcesses = new ArrayList<ProcessBuilder>();
		for(Map.Entry<String, Client> clientEntry :  config.getClients().entrySet()) {
				ProcessBuilder pb = createProcessForClient(config, clientEntry.getValue());
				clientProcesses.add(pb);
		}
		return clientProcesses;

	}
	
	private static ProcessBuilder createProcessForClient(
			Config config, Client client ) {
		Map<String,String> envs = System.getenv();
		ProcessBuilder pb = new ProcessBuilder(
				"java",
				ClientImpl.class.getName(),
				new String(ConfigUtil.convertToBytes(config)),
				client.getClientId());
		pb.environment().putAll(envs);
		return pb;
	}
	
	private static ProcessBuilder createMaster(Config config) {
		Map<String,String> envs = System.getenv();
		ProcessBuilder pb = new ProcessBuilder(
				"java",
				MasterImpl.class.getName(),
				new String(ConfigUtil.convertToBytes(config)),
				config.getMaster().getMasterName());
		pb.environment().putAll(envs);
		return pb;
	}
}
