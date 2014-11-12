package async.chainreplication.app.json.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.activity.InvalidActivityException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import async.chainreplication.application.models.ApplicationRequest;
import async.chainreplication.application.models.ApplicationRequestType;
import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.common.util.Config;
import async.common.util.RequestWithChain;
import async.common.util.TestCases;

// TODO: Auto-generated Javadoc
/**
 * The Class JSONUtility.
 */
public class JSONUtility {

	/**
	 * Generate randomized requests.
	 *
	 * @param client
	 *            the client
	 * @param seed
	 *            the seed
	 * @param probablities
	 *            the probablities
	 * @param numberOfRequests
	 *            the number of requests
	 * @param config
	 *            the config
	 */
	private static void generateRandomizedRequests(Client client, long seed,
			float[] probablities, int numberOfRequests, Config config) {
		final Random random = new Random();
		random.setSeed(seed);
		final List<RequestWithChain> requestWithChains = new ArrayList<RequestWithChain>();
		for (int j = 0; j < probablities.length; j++) {
			final float probability = probablities[j];
			final int noOfRequests = (int) (probability * numberOfRequests);
			for (int i = 0; i < noOfRequests; i++) {
				final String requestId = "req"
						+ random.nextInt(numberOfRequests);
				final ApplicationRequest request = new ApplicationRequest(
						client, requestId);
				request.setRetryCount(random.nextInt(3)); // max 3 retries
				final ApplicationRequestType requestType = ApplicationRequestType
						.values()[j];
				final int accountNum = random.nextInt(Integer.MAX_VALUE);
				final int amount = random.nextInt(100000);// amount restricted
															// to 1
				// lakh
				request.setAccountNum(accountNum);
				if (requestType != ApplicationRequestType.GET_BALANCE)
					request.setAmount(amount);
				final List<String> chainIds = new ArrayList<String>();
				chainIds.addAll(config.getChains().keySet());
				final String chainId = chainIds.get(random
						.nextInt(Integer.MAX_VALUE) % chainIds.size());
				request.setApplicationRequestType(requestType);
				final RequestWithChain requestWithChain = new RequestWithChain(
						request, chainId);
				requestWithChains.add(requestWithChain);
			}
		}
		Collections.shuffle(requestWithChains, random);
		TestCases testCases = config.getTestCases().get(client);
		if (testCases == null) {
			testCases = new TestCases();
			testCases.setClient(client);
		}
		System.out.println("Probability Requests:"
				+ requestWithChains.toString());
		testCases.getRequests().addAll(requestWithChains);
		config.getTestCases().put(client, testCases);
	}

	/**
	 * Read chains.
	 *
	 * @param config
	 *            the config
	 * @param jsonObject
	 *            the json object
	 */
	private static void readChains(Config config, JSONObject jsonObject) {
		final JSONArray jsonArray = (JSONArray) jsonObject.get("Chains");
		@SuppressWarnings("unchecked")
		final Iterator<JSONObject> chainIterator = jsonArray.iterator();
		while (chainIterator.hasNext()) {
			final JSONObject chainObject = chainIterator.next();
			final String chainId = (String) chainObject.get("ChainId");
			final JSONArray serversArray = (JSONArray) chainObject
					.get("Servers");
			final int numberOfServers = Integer.parseInt((String) chainObject
					.get("NumberOfServers"));
			final Map<String, Server> servers = new HashMap<String, Server>();
			for (int i = 0; i < numberOfServers; i++) {
				final JSONObject serverObject = (JSONObject) serversArray
						.get(i);
				final String serverId = (String) serverObject.get("ServerId");
				final String chainName = (String) serverObject.get("ChainId");
				final JSONObject processDetails = (JSONObject) serverObject
						.get("ProcessDetails");
				final String host = (String) processDetails.get("Host");
				final int tcpPort = Integer.parseInt((String) processDetails
						.get("TcpPort"));
				final int udpPort = Integer.parseInt((String) processDetails
						.get("UdpPort"));
				int debugPort = -1;
				if (processDetails.get("DebugPort") != null) {
					debugPort = Integer.parseInt((String) processDetails
							.get("DebugPort"));
				}
				final Server server = new Server(serverId, chainName, host);
				server.getServerProcessDetails().setHost(host);
				server.getServerProcessDetails().setTcpPort(tcpPort);
				server.getServerProcessDetails().setUdpPort(udpPort);
				server.getServerProcessDetails().setDebugPort(debugPort);
				servers.put(serverId, server);

				final String initialSleepTimeString = (String) processDetails
						.get("InitialSleepTime");
				long initialSleepTime = 0;
				if (initialSleepTimeString != null
						&& !initialSleepTimeString.isEmpty()) {
					initialSleepTime = Long.parseLong(initialSleepTimeString);

				}
				config.getServerToInitialSleepTime().put(server,
						initialSleepTime);

				final String timeToLiveString = (String) processDetails
						.get("TimeToLive");
				if (timeToLiveString != null && !timeToLiveString.isEmpty()) {
					final long timeToLive = Long.parseLong(timeToLiveString);
					config.getServerToTimeToLive().put(server, timeToLive);
				}
			}
			/*
			 * Server head = null; Server tail = null; for (int i = 0; i <
			 * numberOfServers; i++) { JSONObject serverObject = (JSONObject)
			 * serversArray.get(i); String serverId = (String)
			 * serverObject.get("ServerId"); String predecessor = (String)
			 * serverObject.get("Predecessor"); String successor = (String)
			 * serverObject.get("Successor"); Server server =
			 * servers.get(serverId); if (!predecessor.isEmpty()) {
			 * server.getAdjacencyList().setPredecessor(
			 * servers.get(predecessor)); } else { head = server; } if
			 * (!successor.isEmpty()) { server.getAdjacencyList().setSucessor(
			 * servers.get(successor)); } else { tail = server; } }
			 */
			final Chain chain = new Chain(chainId, null, null);
			config.getChains().put(chainId, chain);
			config.getChainToServerMap().put(chainId, servers);
		}

	}

	/**
	 * Read clients.
	 *
	 * @param config
	 *            the config
	 * @param jsonObject
	 *            the json object
	 */
	@SuppressWarnings("unchecked")
	private static void readClients(Config config, JSONObject jsonObject) {
		final JSONArray jsonArray = (JSONArray) jsonObject.get("Clients");
		final Iterator<JSONObject> clientIterator = jsonArray.iterator();
		while (clientIterator.hasNext()) {
			final JSONObject clientObject = clientIterator.next();
			final String clientId = (String) clientObject.get("ClientId");
			final JSONObject processDetails = (JSONObject) clientObject
					.get("ProcessDetails");
			final String host = (String) processDetails.get("Host");
			final int tcpPort = Integer.parseInt((String) processDetails
					.get("TcpPort"));
			final int udpPort = Integer.parseInt((String) processDetails
					.get("UdpPort"));
			int debugPort = -1;
			if (processDetails.get("DebugPort") != null) {
				debugPort = Integer.parseInt((String) processDetails
						.get("DebugPort"));
			}
			final int responseWaitTime = Integer.parseInt((String) clientObject
					.get("ResponseWaitTime"));
			final Client client = new Client(clientId, host, responseWaitTime);
			client.getClientProcessDetails().setTcpPort(tcpPort);
			client.getClientProcessDetails().setUdpPort(udpPort);
			client.getClientProcessDetails().setDebugPort(debugPort);
			config.getClients().put(clientId, client);
		}

	}

	/**
	 * Read config from json.
	 *
	 * @param filePath
	 *            the file path
	 * @return the config
	 * @throws InvalidActivityException
	 *             the invalid activity exception
	 */
	public static Config readConfigFromJSON(String filePath)
			throws InvalidActivityException {
		final Config config = new Config();
		FileReader reader = null;
		try {
			reader = new FileReader(new File(filePath));
			final JSONParser jsonParser = new JSONParser();
			final JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			readChains(config, jsonObject);
			readClients(config, jsonObject);
			readRequestTestCases(config, jsonObject);
			readProbablityAndGenerateTestCases(config, jsonObject);
			readMaster(config, jsonObject);
		} catch (IOException | ParseException ex) {
			throw new InvalidActivityException(ex.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return config;
	}

	/**
	 * Read master.
	 *
	 * @param config
	 *            the config
	 * @param jsonObject
	 *            the json object
	 */
	private static void readMaster(Config config, JSONObject jsonObject) {
		final JSONObject masterObject = (JSONObject) jsonObject.get("Master");
		final String masterName = (String) masterObject.get("MasterName");
		final String masterHost = (String) masterObject.get("MasterHost");
		final int masterPort = Integer.parseInt((String) masterObject
				.get("MasterPort"));
		int masterDebugPort = -1;
		if (masterObject.get("MasterDebugPort") != null) {
			masterDebugPort = Integer.parseInt((String) masterObject
					.get("MasterDebugPort"));
		}
		final long heartBeatTimeout = Long.parseLong((String) masterObject
				.get("HeartBeatTimeout"));
		final Master master = new Master(masterHost, masterPort, masterName,
				masterDebugPort);
		master.setHeartbeatTimeout(heartBeatTimeout);
		config.setMaster(master);
	}

	/**
	 * Read probablity and generate test cases.
	 *
	 * @param config
	 *            the config
	 * @param jsonObject
	 *            the json object
	 */
	private static void readProbablityAndGenerateTestCases(Config config,
			JSONObject jsonObject) {
		final JSONArray probablityRequests = (JSONArray) jsonObject
				.get("ProbabilityRequests");
		if (probablityRequests == null || probablityRequests.isEmpty())
			return;
		@SuppressWarnings("unchecked")
		final Iterator<JSONObject> probablityRequestIterator = probablityRequests
				.iterator();
		while (probablityRequestIterator.hasNext()) {
			final JSONObject probabilityRequestObject = probablityRequestIterator
					.next();
			final String clientId = (String) probabilityRequestObject
					.get("ClientId");
			final Client client = config.getClients().get(clientId);
			final int numberOfRequests = Integer
					.parseInt((String) probabilityRequestObject
							.get("NumberOfRequests"));
			final long seed = Long.parseLong((String) probabilityRequestObject
					.get("Seed"));
			final float probablity[] = new float[3];
			final JSONObject probabilityJson = ((JSONObject) probabilityRequestObject
					.get("Probability"));
			probablity[ApplicationRequestType.GET_BALANCE.ordinal()] = Float
					.parseFloat(((String) probabilityJson
							.get(ApplicationRequestType.GET_BALANCE.name())));
			probablity[ApplicationRequestType.WITHDRAW.ordinal()] = Float
					.parseFloat(((String) probabilityJson
							.get(ApplicationRequestType.WITHDRAW.name())));
			probablity[ApplicationRequestType.DEPOSIT.ordinal()] = Float
					.parseFloat(((String) probabilityJson
							.get(ApplicationRequestType.DEPOSIT.name())));
			float total = 0;
			for (int i = 0; i < 3; i++) {
				total += probablity[i];
			}
			assert total == 1;
			generateRandomizedRequests(client, seed, probablity,
					numberOfRequests, config);
		}
	}

	/**
	 * Read request test cases.
	 *
	 * @param config
	 *            the config
	 * @param jsonObject
	 *            the json object
	 */
	private static void readRequestTestCases(Config config,
			JSONObject jsonObject) {
		final JSONArray jsonArray = (JSONArray) jsonObject.get("Requests");
		@SuppressWarnings("unchecked")
		final Iterator<JSONObject> requestIterator = jsonArray.iterator();
		while (requestIterator.hasNext()) {
			final JSONObject requestObject = requestIterator.next();
			final String chainId = (String) requestObject.get("ChainId");
			final String clientId = (String) requestObject.get("ClientId");
			final String requestId = (String) requestObject.get("RequestId");

			final int accountNum = Integer.parseInt((String) requestObject
					.get("AccountNumber"));
			final ApplicationRequestType requestType = ApplicationRequestType
					.valueOf((String) requestObject.get("RequestType"));
			final Client client = config.getClients().get(clientId);
			final ApplicationRequest applicationRequest = new ApplicationRequest(
					client, requestId);
			final String retryCountString = (String) requestObject
					.get("RetryCount");
			if (retryCountString != null && !retryCountString.isEmpty()) {
				applicationRequest.setRetryCount(Integer
						.parseInt(retryCountString));
			}
			applicationRequest.setAccountNum(accountNum);
			final String amountString = (String) requestObject.get("Amount");
			if (amountString != null && !amountString.isEmpty()) {
				final int amount = Integer.parseInt(amountString);
				applicationRequest.setAmount(amount);
			}
			applicationRequest.setApplicationRequestType(requestType);
			applicationRequest.setRequestId(requestId);
			TestCases testCases = config.getTestCases().get(client);
			if (testCases == null) {
				testCases = new TestCases();
			}
			testCases.setClient(client);
			final RequestWithChain requestWithChain = new RequestWithChain(
					applicationRequest, chainId);
			testCases.getRequests().add(requestWithChain);
			config.getTestCases().put(client, testCases);
		}

	}

}