package async.chainreplication.app.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import async.chainreplication.app.json.util.JSONUtility;
import async.chainreplication.client.ClientImpl;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.ServerImpl;
import async.chainreplocation.master.MasterImpl;
import async.common.util.Config;
import async.common.util.ConfigUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AppStarter.
 */
public class AppStarter {

	/**
	 * The Class ServerKiller.
	 */
	private static class ServerKiller extends Thread {

		/** The server to time to die. */
		Map<Server, Long> serverToTimeToDie;

		/** The server to process. */
		Map<Server, Process> serverToProcess;

		/** The kill all servers. */
		volatile boolean killAllServers = false;

		/**
		 * Instantiates a new server killer.
		 *
		 * @param serverToTimeToDie the server to time to die
		 * @param serverToProcess the server to process
		 */
		public ServerKiller(Map<Server, Long> serverToTimeToDie,
				Map<Server, Process> serverToProcess) {
			this.serverToTimeToDie = serverToTimeToDie;
			this.serverToProcess = serverToProcess;
		}

		/**
		 * Kill all servers.
		 */
		public void killAllServers() {
			killAllServers = true;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while (!serverToProcess.isEmpty()) {
				List<Server> serversToKill = new ArrayList<Server>();
				if (!killAllServers) {
					for (Entry<Server, Long> serverToTimeToDieEntry : serverToTimeToDie
							.entrySet()) {
						long timeToDie = serverToTimeToDieEntry.getValue();
						if (timeToDie <= System.currentTimeMillis()) {
							serversToKill.add(serverToTimeToDieEntry.getKey());
						}
					}
				} else {
					serversToKill.addAll(serverToProcess.keySet());
				}
				synchronized (serverToProcess) {
					for (Server serverToKill : serversToKill) {
						Process serverProcess = serverToProcess
								.get(serverToKill);
						if (serverProcess.isAlive()) {
							serverProcess.destroy();
							System.out.println("Server "+serverToKill.getChainName()+
									"-"+serverToKill.getServerId()+" is killed at"+ 
									new Date(System.currentTimeMillis()));
						}
						serverToProcess.remove(serverToKill);
						synchronized (serverToTimeToDie) {
							serverToTimeToDie.remove(serverToKill);
						}
					}
				}
			}
		}
	}

	/**
	 * Creates the clients.
	 *
	 * @param config the config
	 * @return the list
	 */
	private static List<ProcessBuilder> createClients(Config config) {
		List<ProcessBuilder> clientProcesses = new ArrayList<ProcessBuilder>();
		for (Map.Entry<String, Client> clientEntry : config.getClients()
				.entrySet()) {
			ProcessBuilder pb = createProcessForClient(config,
					clientEntry.getValue());
			clientProcesses.add(pb);
		}
		return clientProcesses;

	}

	/**
	 * Creates the master.
	 *
	 * @param config the config
	 * @return the process builder
	 */
	private static ProcessBuilder createMaster(Config config) {
		Map<String, String> envs = System.getenv();
		String classPathValue = System.getProperty("java.class.path");
		List<String> command = new ArrayList<String>();
		command.add("java");
		if(config.getMaster().getMasterDebugPort() != -1) {
			command.add("-Xdebug");
			command.add("-Xrunjdwp:transport=dt_socket,server=y,"
					+ "suspend=y,address="+config.getMaster().getMasterDebugPort());
		}
		command.add(MasterImpl.class.getName());
		command.add(ConfigUtil.serializeToFile(config));
		command.add(config.getMaster().getMasterName());
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}

	/**
	 * Creates the process for client.
	 *
	 * @param config the config
	 * @param client the client
	 * @return the process builder
	 */
	private static ProcessBuilder createProcessForClient(Config config,
			Client client) {
		Map<String, String> envs = System.getenv();
		String classPathValue = System.getProperty("java.class.path");
		List<String> command = new ArrayList<String>();
		command.add("java");
		if(client.getClientProcessDetails().getDebugPort() != -1) {
			command.add("-Xdebug");
			command.add("-Xrunjdwp:transport=dt_socket,server=y,"
					+ "suspend=n,address="+client.getClientProcessDetails().getDebugPort());
		}
		command.add(ClientImpl.class.getName());
		command.add(ConfigUtil.serializeToFile(config));
		command.add(client.getClientId());
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}

	/**
	 * Creates the process for server.
	 *
	 * @param config the config
	 * @param server the server
	 * @return the process builder
	 */
	private static ProcessBuilder createProcessForServer(Config config,
			Server server) {
		Map<String, String> envs = System.getenv();
		String classPathValue = System.getProperty("java.class.path");

		List<String> command = new ArrayList<String>();
		command.add("java");
		if(server.getServerProcessDetails().getDebugPort() != -1) {
			command.add("-Xdebug");
			command.add("-Xrunjdwp:transport=dt_socket,server=y,"
					+ "suspend=y,address="+server.getServerProcessDetails().getDebugPort());
		}
		command.add(ServerImpl.class.getName());
		command.add(ConfigUtil.serializeToFile(config));
		command.add(server.getChainName());
		command.add(server.getServerId());

		ProcessBuilder pb= new ProcessBuilder(command);
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}

	/**
	 * Creates the servers for chains.
	 *
	 * @param config the config
	 * @return the map
	 */
	private static Map<Server, ProcessBuilder> createServersForChains(
			Config config) {
		Map<Server, ProcessBuilder> serverProcesses = new HashMap<Server, ProcessBuilder>();
		for (String chainId : config.getChains().keySet()) {
			for (String serverId : config.getChainToServerMap().get(chainId)
					.keySet()) {
				Server server = config.getChainToServerMap().get(chainId)
						.get(serverId);
				ProcessBuilder pb = createProcessForServer(config, server);
				serverProcesses.put(server, pb);
			}
		}
		return serverProcesses;

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String configFile = args[0];
		ServerKiller serverKiller = null;
		System.out.println("Starting Main " + new Date());
		try {
			Config config = JSONUtility.readConfigFromJSON(configFile);
			ProcessBuilder masterProcessBuilder = createMaster(config);
			Map<Server, ProcessBuilder> serverProcessBuilders = createServersForChains(config);
			List<ProcessBuilder> clientProcessBuilders = createClients(config);
			Map<Server, Process> serverProcesses = new HashMap<Server, Process>();
			Map<Server, Long> serverToTimeToDie = new HashMap<Server, Long>();
			List<Process> clientProcesses = new ArrayList<Process>();
			
			
			Process masterProcess = masterProcessBuilder.start();
			System.out.println("Master Started");
			
			
			for (Entry<Server, ProcessBuilder> pbEntry : serverProcessBuilders
					.entrySet()) {
				Process p = pbEntry.getValue().start();
				serverProcesses.put(pbEntry.getKey(), p);
				Long timeToLive = config.getServerToTimeToLive().get(
						pbEntry.getKey());
				Long initialSleepTime = config.getServerToInitialSleepTime()
						.get(pbEntry.getValue());
				if (timeToLive != null) {
					if (initialSleepTime == null) {
						initialSleepTime = 0L;
					}
					serverToTimeToDie.put(pbEntry.getKey(),
							System.currentTimeMillis()
							+ (timeToLive - initialSleepTime));
				}
			}
			System.out.println("All Servers started");
			
			serverKiller = new ServerKiller(serverToTimeToDie, serverProcesses);
			serverKiller.start();
			
			for (ProcessBuilder pb : clientProcessBuilders) {
				Process p = pb.start();
				clientProcesses.add(p);
			}
			System.out.println("All Clients started");

			while (clientProcesses.size() > 0) {
				Iterator<Process> clientProcessIterator = clientProcesses
						.iterator();
				while (clientProcessIterator.hasNext()) {
					Process p = clientProcessIterator.next();
					if (!p.isAlive()) {
						clientProcessIterator.remove();
					}
				}
			}
			System.out.println("All Clients Died");
			
			
			//serverKiller.killAllServers();
			serverKiller.join();
			System.out.println("All Servers Killed");

			if (masterProcess.isAlive())
				masterProcess.destroy();
			
			System.out.println("Master Killed");
			
			System.out.println("Stopping Main " + new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
