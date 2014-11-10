package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public class Server implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7755158858181878162L;
	
	/** The server id. */
	String serverId;
	
	/** The chain name. */
	String chainName;
	
	/** The server process details. */
	ProcessDetails serverProcessDetails;
	
	/** The adjacency list. */
	AdjacencyList adjacencyList = new AdjacencyList(null, null);

	/**
	 * Instantiates a new server.
	 *
	 * @param serverId the server id
	 * @param chainName the chain name
	 * @param host the host
	 */
	public Server(String serverId, String chainName, String host) {
		this.serverId = serverId;
		this.chainName = chainName;
		serverProcessDetails = new ProcessDetails();
		serverProcessDetails.setHost(host);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
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
		return true;
	}

	/**
	 * Gets the adjacency list.
	 *
	 * @return the adjacency list
	 */
	public AdjacencyList getAdjacencyList() {
		return adjacencyList;
	}

	/**
	 * Gets the chain name.
	 *
	 * @return the chain name
	 */
	public String getChainName() {
		return chainName;
	}

	/**
	 * Gets the server id.
	 *
	 * @return the server id
	 */
	public String getServerId() {
		return serverId;
	}

	/**
	 * Gets the server process details.
	 *
	 * @return the server process details
	 */
	public ProcessDetails getServerProcessDetails() {
		return serverProcessDetails;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chainName == null) ? 0 : chainName.hashCode());
		result = prime * result
				+ ((serverId == null) ? 0 : serverId.hashCode());
		return result;
	}

	/**
	 * Checks if is head.
	 *
	 * @return true, if is head
	 */
	public boolean isHead() {
		return adjacencyList.getPredecessor() == null;
	}

	/**
	 * Checks if is tail.
	 *
	 * @return true, if is tail
	 */
	public boolean isTail() {
		return adjacencyList.getSucessor() == null;
	}

	/**
	 * Sets the adjacency list.
	 *
	 * @param adjacencyList the new adjacency list
	 */
	public void setAdjacencyList(AdjacencyList adjacencyList) {
		this.adjacencyList = adjacencyList;
	}

	/**
	 * Sets the bank name.
	 *
	 * @param bankName the new bank name
	 */
	public void setBankName(String bankName) {
		chainName = bankName;
	}

	/**
	 * Sets the server id.
	 *
	 * @param serverId the new server id
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Server [serverId=" + serverId + ", chainName=" + chainName
				+ ", serverProcessDetails=" + serverProcessDetails.toString()
				+ ", adjacencyList=" + adjacencyList.toString() + "]";
	}

}
