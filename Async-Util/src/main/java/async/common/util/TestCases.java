package async.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import async.chainreplication.master.models.Client;

public class TestCases implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -397223799879846009L;
	Client client;
	List<RequestWithChain> requests = new ArrayList<RequestWithChain>();
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
	public List<RequestWithChain> getRequests() {
		return requests;
	}
	@Override
	public String toString() {
		return "TestCases [client=" + client + "," + requests.toString() + "]";
	}
	
	
	
}