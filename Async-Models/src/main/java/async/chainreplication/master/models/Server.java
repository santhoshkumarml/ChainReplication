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
	AdjacencyList adjacencyList = new AdjacencyList(null, null);
	
	public Server(String serverId, String chainName, String host) {
		this.serverId = serverId;
		this.chainName = chainName;
		this.serverProcessDetails = new ProcessDetails();
		this.serverProcessDetails.setHost(host);
	}
	
	public String getServerId() {
		return serverId;
	}
	public String getChainName() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adjacencyList == null) ? 0 : adjacencyList.hashCode());
		result = prime * result
				+ ((chainName == null) ? 0 : chainName.hashCode());
		result = prime * result
				+ ((serverId == null) ? 0 : serverId.hashCode());
		result = prime
				* result
				+ ((serverProcessDetails == null) ? 0 : serverProcessDetails
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		if (adjacencyList == null) {
			if (other.adjacencyList != null)
				return false;
		} else if (!adjacencyList.equals(other.adjacencyList))
			return false;
		if (chainName == null) {
			if (other.chainName != null)
				return false;
		} else if (!chainName.equals(other.chainName))
			return false;
		if (serverId == null) {
			if (other.serverId != null)
				return false;
		} else if (!serverId.equals(other.serverId))
			return false;
		if (serverProcessDetails == null) {
			if (other.serverProcessDetails != null)
				return false;
		} else if (!serverProcessDetails.equals(other.serverProcessDetails))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Server [serverId=" + serverId + ", chainName=" + chainName
				+ ", serverProcessDetails=" + serverProcessDetails.toString()
				+ ", adjacencyList=" + adjacencyList.toString() + "]";
	}
	
	
	
}
