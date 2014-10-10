package async.common.util;

import java.util.ArrayList;
import java.util.List;

import async.server.client.models.Request;;

public class Config {
	
	List<Bank> banks = new ArrayList<Config.Bank>();
	List<Client> client = new ArrayList<Config.Client>();
	
	private class Bank {
		String name;
		int chainLength;
	}
	
	private class Client {
		int no;
	}
	
	private class Master {
		long heartBeatTimeOut;
	}
	
	private class TestCases {
		Client client;
		Request request;
	}

}
