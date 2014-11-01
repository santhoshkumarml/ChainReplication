package async.chainreplication.app.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.chainreplication.app.json.util.JSONUtility;
import async.chainreplication.client.ClientImpl;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.ServerImpl;
import async.chainreplocation.master.MasterImpl;
import async.common.util.Config;
import async.common.util.ConfigUtil;

public class AppStarter {
	public static void main(String[] args) {
		String configFile = args[0];
		ServerKiller serverKiller = null;
		System.out.println("Starting Main "+new Date());
		try{
			Config config = JSONUtility.readConfigFromJSON(configFile);
			ProcessBuilder masterProcessBuilder = createMaster(config);
			Map<Server, ProcessBuilder> serverProcessBuilders = createServersForChains(config);
			List<ProcessBuilder> clientProcessBuilders = createClients(config);
			Map<Server, Process> serverProcesses = new HashMap<Server, Process>();
			Map<Server, Long> serverToTimeToDie = new HashMap<Server, Long>();
			List<Process> clientProcesses = new ArrayList<Process>();
			Process masterProcess = masterProcessBuilder.start();
			for(Entry<Server, ProcessBuilder> pbEntry : serverProcessBuilders.entrySet()) {
				Process p =pbEntry.getValue().start();
				serverProcesses.put(pbEntry.getKey(), p);
				Integer timeToLive = config.getServerToTimeToLive().get(pbEntry.getKey());
				serverToTimeToDie.put(pbEntry.getKey(), System.currentTimeMillis()+timeToLive);
			}
			serverKiller = new ServerKiller(serverToTimeToDie, serverProcesses);
			serverKiller.start();
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
			serverKiller.killAllServers();
			serverKiller.join();

			if(masterProcess.isAlive())
				masterProcess.destroy();

			System.out.println("Stopping Main "+new Date());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(serverKiller != null) {
				serverKiller.stopThread();
			}
		}
	}

	private static Map<Server, ProcessBuilder> createServersForChains(
			Config config) {
		Map<Server, ProcessBuilder> serverProcesses = new HashMap<Server, ProcessBuilder>();
		for(String chainId : config.getChains().keySet()) {
			for(String serverId : config.getChainToServerMap().get(chainId).keySet()) {
				Server server =  config.getChainToServerMap().get(chainId).get(serverId);
				ProcessBuilder pb = createProcessForServer(config, server);
				serverProcesses.put(server, pb);
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


	private static class ServerKiller extends Thread {

		Map<Server, Long> serverToTimeToDie;
		Map<Server,Process> serverToProcess;
		volatile boolean stopThread = false;
		volatile boolean killAllServers = false;

		public ServerKiller(Map<Server, Long> serverToTimeToDie, Map<Server,Process> serverToProcess) {
			this.serverToTimeToDie = serverToTimeToDie;
			this.serverToProcess = serverToProcess;
		}

		public void killAllServers() {
			killAllServers = true;
		}

		public void stopThread() {
			stopThread = true;
		}

		@Override
		public void run() {
			while(!stopThread &&
					!this.serverToTimeToDie.isEmpty() &&
					!this.serverToProcess.isEmpty()) {
				List<Server> serversToKill = new ArrayList<Server>();
				if(!killAllServers) {
					for(Entry<Server, Long> serverToTimeToDieEntry : this.serverToTimeToDie.entrySet()) {
						long timeToDie = serverToTimeToDieEntry.getValue();
						if(timeToDie<=System.currentTimeMillis()) {
							serversToKill.add(serverToTimeToDieEntry.getKey());   
						}
					}
				} else {
					serversToKill.addAll(serverToTimeToDie.keySet());
				}
				synchronized (serverToProcess) {
					for(Server serverToKill : serversToKill) {
						Process serverProcess = serverToProcess.get(serverToKill);
						if(serverProcess.isAlive()) {
							serverProcess.destroy();
						}
						serverToProcess.remove(serverToKill);
						synchronized (serverToTimeToDie) {
							this.serverToTimeToDie.remove(serverToKill);
						}
					}	
				}
			}
		}
	}
}
