package async.common.util;

import java.util.ArrayList;
import java.util.List;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Client;

public class TestCases {
	Client client;
	List<Request> requests = new ArrayList<Request>();
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public List<Request> getRequests() {
		return requests;
	}
	
	
}