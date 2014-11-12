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
		 * @param serverToTimeToDie
		 *            the server to time to die
		 * @param serverToProcess
		 *            the server to process
		 */
		public ServerKiller(Map<Server, Long> serverToTimeToDie,
				Map<Server, Process> serverToProcess) {
			this.serverToTimeToDie = serverToTimeToDie;
			this.serverToProcess = serverToProcess;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while (!serverToProcess.isEmpty()) {
				final List<Server> serversToKill = new ArrayList<Server>();
				if (!killAllServers) {
					for (final Entry<Server, Long> serverToTimeToDieEntry : serverToTimeToDie
							.entrySet()) {
						final long timeToDie = serverToTimeToDieEntry
								.getValue();
						if (timeToDie <= System.currentTimeMillis()) {
							serversToKill.add(serverToTimeToDieEntry.getKey());
						}
					}
				} else {
					serversToKill.addAll(serverToProcess.keySet());
				}
				synchronized (serverToProcess) {
					for (final Server serverToKill : serversToKill) {
						final Process serverProcess = serverToProcess
								.get(serverToKill);
						if (serverProcess.isAlive()) {
							serverProcess.destroy();
							System.out.println("Server "
									+ serverToKill.getChainName() + "-"
									+ serverToKill.getServerId()
									+ " is killed at"
									+ new Date(System.currentTimeMillis()));
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
	 * @param config
	 *            the config
	 * @return the list
	 */
	private static List<ProcessBuilder> createClients(Config config) {
		final List<ProcessBuilder> clientProcesses = new ArrayList<ProcessBuilder>();
		for (final Map.Entry<String, Client> clientEntry : config.getClients()
				.entrySet()) {
			final ProcessBuilder pb = createProcessForClient(config,
					clientEntry.getValue());
			clientProcesses.add(pb);
		}
		return clientProcesses;

	}

	/**
	 * Creates the master.
	 *
	 * @param config
	 *            the config
	 * @return the process builder
	 */
	private static ProcessBuilder createMaster(Config config) {
		final Map<String, String> envs = System.getenv();
		final String classPathValue = System.getProperty("java.class.path");
		final List<String> command = new ArrayList<String>();
		command.add("java");
		if (config.getMaster().getMasterDebugPort() != -1) {
			command.add("-Xdebug");
			command.add("-Xrunjdwp:transport=dt_socket,server=y,"
					+ "suspend=y,address="
					+ config.getMaster().getMasterDebugPort());
		}
		command.add(MasterImpl.class.getName());
		command.add(ConfigUtil.serializeToFile(config));
		command.add(config.getMaster().getMasterName());
		final ProcessBuilder pb = new ProcessBuilder(command);
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}

	/**
	 * Creates the process for client.
	 *
	 * @param config
	 *            the config
	 * @param client
	 *            the client
	 * @return the process builder
	 */
	private static ProcessBuilder createProcessForClient(Config config,
			Client client) {
		final Map<String, String> envs = System.getenv();
		final String classPathValue = System.getProperty("java.class.path");
		final List<String> command = new ArrayList<String>();
		command.add("java");
		if (client.getClientProcessDetails().getDebugPort() != -1) {
			command.add("-Xdebug");
			command.add("-Xrunjdwp:transport=dt_socket,server=y,"
					+ "suspend=n,address="
					+ client.getClientProcessDetails().getDebugPort());
		}
		command.add(ClientImpl.class.getName());
		command.add(ConfigUtil.serializeToFile(config));
		command.add(client.getClientId());
		final ProcessBuilder pb = new ProcessBuilder(command);
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}

	/**
	 * Creates the process for server.
	 *
	 * @param config
	 *            the config
	 * @param server
	 *            the server
	 * @return the process builder
	 */
	private static ProcessBuilder createProcessForServer(Config config,
			Server server) {
		final Map<String, String> envs = System.getenv();
		final String classPathValue = System.getProperty("java.class.path");

		final List<String> command = new ArrayList<String>();
		command.add("java");
		if (server.getServerProcessDetails().getDebugPort() != -1) {
			command.add("-Xdebug");
			command.add("-Xrunjdwp:transport=dt_socket,server=y,"
					+ "suspend=y,address="
					+ server.getServerProcessDetails().getDebugPort());
		}
		command.add(ServerImpl.class.getName());
		command.add(ConfigUtil.serializeToFile(config));
		command.add(server.getChainName());
		command.add(server.getServerId());

		final ProcessBuilder pb = new ProcessBuilder(command);
		pb.environment().putAll(envs);
		pb.environment().put("CLASSPATH", classPathValue);
		return pb;
	}

	/**
	 * Creates the servers for chains.
	 *
	 * @param config
	 *            the config
	 * @return the map
	 */
	private static Map<Server, ProcessBuilder> createServersForChains(
			Config config) {
		final Map<Server, ProcessBuilder> serverProcesses = new HashMap<Server, ProcessBuilder>();
		for (final String chainId : config.getChains().keySet()) {
			for (final String serverId : config.getChainToServerMap()
					.get(chainId).keySet()) {
				final Server server = config.getChainToServerMap().get(chainId)
						.get(serverId);
				final ProcessBuilder pb = createProcessForServer(config, server);
				serverProcesses.put(server, pb);
			}
		}
		return serverProcesses;

	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		final String configFile = args[0];
		ServerKiller serverKiller = null;
		System.out.println("Starting Main " + new Date());
		try {
			final Config config = JSONUtility.readConfigFromJSON(configFile);
			final ProcessBuilder masterProcessBuilder = createMaster(config);
			final Map<Server, ProcessBuilder> serverProcessBuilders = createServersForChains(config);
			final List<ProcessBuilder> clientProcessBuilders = createClients(config);
			final Map<Server, Process> serverProcesses = new HashMap<Server, Process>();
			final Map<Server, Long> serverToTimeToDie = new HashMap<Server, Long>();
			final List<Process> clientProcesses = new ArrayList<Process>();

			final Process masterProcess = masterProcessBuilder.start();
			System.out.println("Master Started");

			for (final Entry<Server, ProcessBuilder> pbEntry : serverProcessBuilders
					.entrySet()) {
				final Process p = pbEntry.getValue().start();
				serverProcesses.put(pbEntry.getKey(), p);
				final Long timeToLive = config.getServerToTimeToLive().get(
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

			for (final ProcessBuilder pb : clientProcessBuilders) {
				final Process p = pb.start();
				clientProcesses.add(p);
			}
			System.out.println("All Clients started");

			while (clientProcesses.size() > 0) {
				final Iterator<Process> clientProcessIterator = clientProcesses
						.iterator();
				while (clientProcessIterator.hasNext()) {
					final Process p = clientProcessIterator.next();
					if (!p.isAlive()) {
						clientProcessIterator.remove();
					}
				}
			}
			System.out.println("All Clients Died");

			// serverKiller.killAllServers();
			serverKiller.join();
			System.out.println("All Servers Killed");

			if (masterProcess.isAlive())
				masterProcess.destroy();

			System.out.println("Master Killed");

			System.out.println("Stopping Main " + new Date());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
