package async.chainreplication.app.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import app.chainreplication.app.json.util.JSONUtility;
import async.chainreplication.client.ClientImpl;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.ServerImpl;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import async.master.MasterImpl;

public class AppStarter {
	public static void main(String[] args) {
		String configFile = args[0];
		System.out.println("Starting Main"+new Date());
		try{
			Config config = JSONUtility.readConfigFromJSON(configFile);
			ProcessBuilder masterProcessBuilder = createMaster(config);
			List<ProcessBuilder> serverProcessBuilders = createServersForChains(config);
			List<ProcessBuilder> clientProcessBuilders = createClients(config);
			List<Process> serverProcesses = new ArrayList<Process>();
			List<Process> clientProcesses = new ArrayList<Process>();
			Process masterProcess = masterProcessBuilder.start();
			for(ProcessBuilder pb : serverProcessBuilders) {
				Process p =pb.start();
				serverProcesses.add(p);
			}
			for(ProcessBuilder pb : clientProcessBuilders) {
				Process p = pb.start();
				clientProcesses.add(p);
			}

			while(clientProcesses.size() > 0) {
				Iterator<Process> clientProcessIterator = clientProcesses.iterator();
				while(clientProcessIterator.hasNext()) {
					Process p = clientProcessIterator.next();
					if(!p.isAlive()) {
						clientProcessIterator.remove();
					}
				}
			}

			for(Process serverProcess : serverProcesses) {
				if(serverProcess.isAlive())
					serverProcess.destroy();
			}
			if(masterProcess.isAlive())
				masterProcess.destroy();
			System.out.println("Stopping Main"+new Date());
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
		String classPathValue = System.getProperty("java.class.path");
		ProcessBuilder pb;
		if(server.isTail()) {
		pb = new ProcessBuilder(
				"java",
				//"-Xdebug",
				//"-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=13000",
				ServerImpl.class.getName(),
				ConfigUtil.serializeToFile(config),
				server.getChainName(),
				server.getServerId());
		} else {
			pb = new ProcessBuilder(
					"java",
					//"-Xdebug",
					//"-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=13000",
					ServerImpl.class.getName(),
					ConfigUtil.serializeToFile(config),
					server.getChainName(),
					server.getServerId());
			
		}
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
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
		String classPathValue = System.getProperty("java.class.path");
		ProcessBuilder pb = new ProcessBuilder(
				"java",
				//"-Xdebug",
				//"-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=13001",
				ClientImpl.class.getName(),
				ConfigUtil.serializeToFile(config),
				client.getClientId());
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
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
