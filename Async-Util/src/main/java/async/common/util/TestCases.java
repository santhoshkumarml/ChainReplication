package async.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Client;

public class TestCases implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -397223799879846009L;
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