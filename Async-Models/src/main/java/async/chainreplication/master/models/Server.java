package async.chainreplication.master.models;

import java.io.Serializable;


public class Server implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7755158858181878162L;
	String serverId;
	String bankName;
	ProcessDetails serverProcessDetails;
	AdjacencyList adjacencyList;
	
	public Server(String serverId, String bankName, String host,int port) {
		this.serverId = serverId;
		this.bankName = bankName;
		this.serverProcessDetails = new ProcessDetails();
		this.serverProcessDetails.setHost(host);
		this.serverProcessDetails.setPort(port);
	}
	
	public String getServerId() {
		return serverId;
	}
	public String getBankName() {
		return bankName;
	}
	public ProcessDetails getServerProcessDetails() {
		return serverProcessDetails;
	}
	public AdjacencyList getAdjacencyList() {
		return adjacencyList;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public void setAdjacencyList(AdjacencyList adjacencyList) {
		this.adjacencyList = adjacencyList;
	}
	
	
	public boolean isHead() {
		return this.adjacencyList.getPredecessor() == null;
	}
	
	public boolean isTail() {
		return this.adjacencyList.getSucessor() == null;
	}
	
}
