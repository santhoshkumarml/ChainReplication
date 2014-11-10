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
	 * @param client the client
	 * @param seed the seed
	 * @param probablities the probablities
	 * @param numberOfRequests the number of requests
	 * @param config the config
	 */
	private static void generateRandomizedRequests(Client client, long seed,
			float[] probablities, int numberOfRequests, Config config) {
		Random random = new Random();
		random.setSeed(seed);
		List<RequestWithChain> requestWithChains = new ArrayList<RequestWithChain>();
		for (int j = 0; j < probablities.length; j++) {
			float probability = probablities[j];
			int noOfRequests = (int) (probability * numberOfRequests);
			for (int i = 0; i < noOfRequests; i++) {
				String requestId = "req" + random.nextInt(numberOfRequests);
				ApplicationRequest request = new ApplicationRequest(client,
						requestId);
				request.setRetryCount(random.nextInt(3)); // max 3 retries
				ApplicationRequestType requestType = ApplicationRequestType
						.values()[j];
				int accountNum = random.nextInt(Integer.MAX_VALUE);
				int amount = random.nextInt(100000);// amount restricted to 1
													// lakh
				request.setAccountNum(accountNum);
				if (requestType != ApplicationRequestType.GET_BALANCE)
					request.setAmount(amount);
				List<String> chainIds = new ArrayList<String>();
				chainIds.addAll(config.getChains().keySet());
				String chainId = chainIds.get(random.nextInt(Integer.MAX_VALUE)
						% chainIds.size());
				request.setApplicationRequestType(requestType);
				RequestWithChain requestWithChain = new RequestWithChain(
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
	 * @param config the config
	 * @param jsonObject the json object
	 */
	private static void readChains(Config config, JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("Chains");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> chainIterator = jsonArray.iterator();
		while (chainIterator.hasNext()) {
			JSONObject chainObject = chainIterator.next();
			String chainId = (String) chainObject.get("ChainId");
			JSONArray serversArray = (JSONArray) chainObject.get("Servers");
			int numberOfServers = Integer.parseInt((String) chainObject
					.get("NumberOfServers"));
			Map<String, Server> servers = new HashMap<String, Server>();
			for (int i = 0; i < numberOfServers; i++) {
				JSONObject serverObject = (JSONObject) serversArray.get(i);
				String serverId = (String) serverObject.get("ServerId");
				String chainName = (String) serverObject.get("ChainId");
				JSONObject processDetails = (JSONObject) serverObject
						.get("ProcessDetails");
				String host = (String) processDetails.get("Host");
				int tcpPort = Integer.parseInt((String) processDetails
						.get("TcpPort"));
				int udpPort = Integer.parseInt((String) processDetails
						.get("UdpPort"));
				Server server = new Server(serverId, chainName, host);
				server.getServerProcessDetails().setHost(host);
				server.getServerProcessDetails().setTcpPort(tcpPort);
				server.getServerProcessDetails().setUdpPort(udpPort);
				servers.put(serverId, server);

				String initialSleepTime = (String) processDetails
						.get("InitialSleepTime");
				if (initialSleepTime != null && !initialSleepTime.isEmpty()) {
					long timeToLive = Long.parseLong(initialSleepTime);
					config.getServerToTimeToLive().put(server, timeToLive);
				}
				String timeToLiveString = (String) processDetails
						.get("TimeToLive");
				if (timeToLiveString != null && !timeToLiveString.isEmpty()) {
					long timeToLive = Long.parseLong(timeToLiveString);
					config.getServerToTimeToLive().put(server, timeToLive);
				}
			}
			Server head = null;
			Server tail = null;
			for (int i = 0; i < numberOfServers; i++) {
				JSONObject serverObject = (JSONObject) serversArray.get(i);
				String serverId = (String) serverObject.get("ServerId");
				String predecessor = (String) serverObject.get("Predecessor");
				String successor = (String) serverObject.get("Successor");
				Server server = servers.get(serverId);
				if (!predecessor.isEmpty()) {
					server.getAdjacencyList().setPredecessor(
							servers.get(predecessor));
				} else {
					head = server;
				}
				if (!successor.isEmpty()) {
					server.getAdjacencyList().setSucessor(
							servers.get(successor));
				} else {
					tail = server;
				}
			}
			Chain chain = new Chain(chainId, head, tail);
			config.getChains().put(chainId, chain);
			config.getChainToServerMap().put(chainId, servers);
		}

	}

	/**
	 * Read clients.
	 *
	 * @param config the config
	 * @param jsonObject the json object
	 */
	@SuppressWarnings("unchecked")
	private static void readClients(Config config, JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("Clients");
		Iterator<JSONObject> clientIterator = jsonArray.iterator();
		while (clientIterator.hasNext()) {
			JSONObject clientObject = clientIterator.next();
			String clientId = (String) clientObject.get("ClientId");
			JSONObject processDetails = (JSONObject) clientObject
					.get("ProcessDetails");
			String host = (String) processDetails.get("Host");
			int udpPort = Integer.parseInt((String) processDetails
					.get("UdpPort"));
			int responseWaitTime = Integer.parseInt((String) clientObject
					.get("ResponseWaitTime"));
			Client client = new Client(clientId, host, responseWaitTime);
			client.getClientProcessDetails().setUdpPort(udpPort);
			config.getClients().put(clientId, client);
		}

	}

	/**
	 * Read config from json.
	 *
	 * @param filePath the file path
	 * @return the config
	 * @throws InvalidActivityException 
	 */
	public static Config readConfigFromJSON(String filePath) throws InvalidActivityException {
		Config config = new Config();
		FileReader reader = null;
		try {
			reader = new FileReader(new File(filePath));
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			readChains(config, jsonObject);
			readClients(config, jsonObject);
			readRequestTestCases(config, jsonObject);
			readProbablityAndGenerateTestCases(config, jsonObject);
			readMaster(config, jsonObject);
		} catch (IOException | ParseException ex) {
			throw new InvalidActivityException(ex.getMessage()) ;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return config;
	}

	/**
	 * Read master.
	 *
	 * @param config the config
	 * @param jsonObject the json object
	 */
	private static void readMaster(Config config, JSONObject jsonObject) {
		JSONObject masterObject = (JSONObject) jsonObject.get("Master");
		String masterName = (String) masterObject.get("MasterName");
		String masterHost = (String) masterObject.get("MasterHost");
		int masterPort = Integer.parseInt((String) masterObject
				.get("MasterPort"));
		long heartBeatTimeout = Long.parseLong((String) masterObject
				.get("HeartBeatTimeout"));
		Master master = new Master(masterHost, masterPort, masterName);
		master.setHeartbeatTimeout(heartBeatTimeout);
		config.setMaster(master);
	}

	/**
	 * Read probablity and generate test cases.
	 *
	 * @param config the config
	 * @param jsonObject the json object
	 */
	private static void readProbablityAndGenerateTestCases(Config config,
			JSONObject jsonObject) {
		JSONArray probablityRequests = (JSONArray) jsonObject
				.get("ProbabilityRequests");
		if (probablityRequests == null || probablityRequests.isEmpty())
			return;
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> probablityRequestIterator = probablityRequests
				.iterator();
		while (probablityRequestIterator.hasNext()) {
			JSONObject probabilityRequestObject = probablityRequestIterator
					.next();
			String clientId = (String) probabilityRequestObject.get("ClientId");
			Client client = config.getClients().get(clientId);
			int numberOfRequests = Integer
					.parseInt((String) probabilityRequestObject
							.get("NumberOfRequests"));
			long seed = Long.parseLong((String) probabilityRequestObject
					.get("Seed"));
			float probablity[] = new float[3];
			JSONObject probabilityJson = ((JSONObject) probabilityRequestObject
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
	 * @param config the config
	 * @param jsonObject the json object
	 */
	private static void readRequestTestCases(Config config,
			JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("Requests");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> requestIterator = jsonArray.iterator();
		while (requestIterator.hasNext()) {
			JSONObject requestObject = requestIterator.next();
			String chainId = (String) requestObject.get("ChainId");
			String clientId = (String) requestObject.get("ClientId");
			String requestId = (String) requestObject.get("RequestId");

			int accountNum = Integer.parseInt((String) requestObject
					.get("AccountNumber"));
			ApplicationRequestType requestType = ApplicationRequestType
					.valueOf((String) requestObject.get("RequestType"));
			Client client = config.getClients().get(clientId);
			ApplicationRequest applicationRequest = new ApplicationRequest(
					client, requestId);
			String retryCountString = (String) requestObject.get("RetryCount");
			if (retryCountString != null && !retryCountString.isEmpty()) {
				applicationRequest.setRetryCount(Integer
						.parseInt(retryCountString));
			}
			applicationRequest.setAccountNum(accountNum);
			String amountString = (String) requestObject.get("Amount");
			if (amountString != null && !amountString.isEmpty()) {
				int amount = Integer.parseInt(amountString);
				applicationRequest.setAmount(amount);
			}
			applicationRequest.setApplicationRequestType(requestType);
			applicationRequest.setRequestId(requestId);
			TestCases testCases = config.getTestCases().get(client);
			if (testCases == null) {
				testCases = new TestCases();
			}
			testCases.setClient(client);
			RequestWithChain requestWithChain = new RequestWithChain(
					applicationRequest, chainId);
			testCases.getRequests().add(requestWithChain);
			config.getTestCases().put(client, testCases);
		}

	}

}