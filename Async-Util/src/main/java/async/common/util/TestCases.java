package async.common.util;

import java.util.ArrayList;
import java.util.List;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Client;

public class TestCases {
	Client client;
	String chainName;
	List<Request> requests = new ArrayList<Request>();
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	public List<Request> getRequests() {
		return requests;
	}
	
}