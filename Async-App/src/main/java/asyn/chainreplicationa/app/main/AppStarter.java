package asyn.chainreplicationa.app.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
			ProcessBuilder masterProcessBuilder = createMaster(config);
			List<ProcessBuilder> serverProcessBuilders = createServersForChains(config);
			List<ProcessBuilder> clientProcessBuilders = createClients(config);
			masterProcessBuilder.start();
			for(ProcessBuilder pb : serverProcessBuilders) {
				Process p =pb.start();
				BufferedReader is;  // reader for output of process
				String line;

				// getInputStream gives an Input stream connected to
				// the process standard output. Just use it to make
				// a BufferedReader to readLine() what the program writes out.
				is = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while ((line = is.readLine()) != null)
				  System.out.println(line);
				p.waitFor();
			}
			for(ProcessBuilder pb : clientProcessBuilders) {
				pb.start();
			}
			
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
				"java",//"-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=10000",
				ServerImpl.class.getName(),
				ConfigUtil.serializeToFile(config),
				server.getChainName(),
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
				ConfigUtil.serializeToFile(config),
				client.getClientId());
		pb.environment().putAll(envs);
		return pb;
	}
	
	private static ProcessBuilder createMaster(Config config) {
		Map<String,String> envs = System.getenv();
		String classPathValue = System.getProperty("java.class.path");
		ProcessBuilder pb = new ProcessBuilder(
				"java",
				MasterImpl.class.getName(),
				ConfigUtil.serializeToFile(config),
				config.getMaster().getMasterName());
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}
}
