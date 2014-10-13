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
import async.common.util.TestCases;

public class AppConfigUtil {

	public static Config readConfigFromFile(String configFile) {
		Config config = Config.createDefaultValues();
		readTestCases(config, configFile);
		return config;
	}

	private static void readTestCases(Config config, String configFile) {
		BufferedReader br = null;
		try{
			br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(new File(configFile))));
			String input = "";
			while((input = br.readLine()) != null) {
				String[] inputSplit = input.split(":");
				String clientId = inputSplit[0].trim();
				Client client =config.getClients().get(clientId);
				String requestString = inputSplit[1].trim();
				String[] requestStringSplit = requestString.split(",");
				ApplicationRequest request = createRequest(client, requestStringSplit);
				TestCases testCases = config.getTestCases().get(client);
				if(testCases == null) {
					testCases = new TestCases();
					String chainName = requestStringSplit[0].trim();
					testCases.setClient(client);
					testCases.setChainName(chainName);
				}
				testCases.getRequests().add(request);
				config.getTestCases().put(client, testCases);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private static ApplicationRequest createRequest(Client client, String[] requestStringSplit) {
		String reqId = requestStringSplit[2].trim();
		String accountNum = requestStringSplit[3].trim();
		ApplicationRequestType applicationRequestType = 
				ApplicationRequestType.valueOf(requestStringSplit[1].toUpperCase().trim());
		ApplicationRequest applicationRequest  = new ApplicationRequest(client, reqId);
		applicationRequest.setApplicationRequestType(applicationRequestType);
		applicationRequest.setAccountNum(Integer.valueOf(accountNum));
		switch (applicationRequestType) {
		case GET_BALANCE:
			break;
		case TRANSFER:
			int amount = Integer.valueOf(requestStringSplit[4].trim());
			applicationRequest.setAmount(amount);
			String destBank = requestStringSplit[5];
			applicationRequest.setDestBank(destBank);
			int destAccountNum = Integer.valueOf(requestStringSplit[6].trim());
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

}
