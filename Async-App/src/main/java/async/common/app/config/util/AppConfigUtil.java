package async.common.app.config.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import async.chainreplication.application.models.ApplicationRequest;
import async.chainreplication.application.models.ApplicationRequestType;
import async.chainreplication.master.models.Client;
import async.common.util.Config;
import async.common.util.RequestWithChain;
import async.common.util.TestCases;

// TODO: Auto-generated Javadoc
/**
 * The Class AppConfigUtil.
 */
public class AppConfigUtil {

	/**
	 * Creates the request.
	 *
	 * @param client
	 *            the client
	 * @param requestStringSplit
	 *            the request string split
	 * @return the application request
	 */
	private static ApplicationRequest createRequest(Client client,
			String[] requestStringSplit) {
		final String reqId = requestStringSplit[2].trim();
		final String accountNum = requestStringSplit[3].trim();
		final ApplicationRequestType applicationRequestType = ApplicationRequestType
				.valueOf(requestStringSplit[1].toUpperCase().trim());
		final ApplicationRequest applicationRequest = new ApplicationRequest(
				client, reqId);
		applicationRequest.setApplicationRequestType(applicationRequestType);
		applicationRequest.setAccountNum(Integer.valueOf(accountNum));
		switch (applicationRequestType) {
		case GET_BALANCE:
			break;
		case TRANSFER:
			int amount = Integer.valueOf(requestStringSplit[4].trim());
			applicationRequest.setAmount(amount);
			final String destBank = requestStringSplit[5];
			applicationRequest.setDestBank(destBank);
			final int destAccountNum = Integer.valueOf(requestStringSplit[6]
					.trim());
			applicationRequest.setAmount(destAccountNum);
			break;
		case DEPOSIT:
			amount = Integer.valueOf(requestStringSplit[4].trim());
			applicationRequest.setAmount(amount);
			break;
		case WITHDRAW:
			amount = Integer.valueOf(requestStringSplit[4].trim());
			applicationRequest.setAmount(amount);
			break;
		default:
			break;
		}

		return applicationRequest;
	}

	/**
	 * Read config from file.
	 *
	 * @param configFile
	 *            the config file
	 * @return the config
	 */
	public static Config readConfigFromFile(String configFile) {
		final Config config = new Config();
		// Config.createDefaultValues();
		readTestCases(config, configFile);
		return config;
	}

	/**
	 * Read test cases.
	 *
	 * @param config
	 *            the config
	 * @param configFile
	 *            the config file
	 */
	private static void readTestCases(Config config, String configFile) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(configFile))));
			String input = "";
			while ((input = br.readLine()) != null) {
				final String[] inputSplit = input.split(":");
				final String clientId = inputSplit[0].trim();
				final Client client = config.getClients().get(clientId);
				final String requestString = inputSplit[1].trim();
				final String[] requestStringSplit = requestString.split(",");
				final ApplicationRequest request = createRequest(client,
						requestStringSplit);
				TestCases testCases = config.getTestCases().get(client);
				final String chainName = requestStringSplit[0].trim();
				if (testCases == null) {
					testCases = new TestCases();
					testCases.setClient(client);
				}
				testCases.getRequests().add(
						new RequestWithChain(request, chainName));
				config.getTestCases().put(client, testCases);
			}

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
