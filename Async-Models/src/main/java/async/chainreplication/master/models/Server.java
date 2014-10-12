package async.chainreplication.master.models;

import java.io.Serializable;


public class Server implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7755158858181878162L;
	String serverId;
	String chainName;
	ProcessDetails serverProcessDetails;
	AdjacencyList adjacencyList;
	
	public Server(String serverId, String chainName, String host,int port) {
		this.serverId = serverId;
		this.chainName = chainName;
		this.serverProcessDetails = new ProcessDetails();
		this.serverProcessDetails.setHost(host);
		this.serverProcessDetails.setPort(port);
	}
	
	public String getServerId() {
		return serverId;
	}
	public String getBankName() {
		return chainName;
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
		this.chainName = bankName;
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

	@Override
	public String toString() {
		return "Server [serverId=" + serverId + ", chainName=" + chainName
				+ ", serverProcessDetails=" + serverProcessDetails.toString()
				+ ", adjacencyList=" + adjacencyList.toString() + "]";
	}
	
	
	
}
