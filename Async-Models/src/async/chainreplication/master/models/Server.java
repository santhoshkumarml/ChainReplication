package async.chainreplication.master.models;

import java.io.Serializable;


public class Server implements Serializable{
	String serverId;
	String bankName;
	ServerProcessDetails serverProcessDetails;
	AdjacencyList adjacencyList;
	
	
	public String getServerId() {
		return serverId;
	}
	public String getBankName() {
		return bankName;
	}
	public ServerProcessDetails getServerProcessDetails() {
		return serverProcessDetails;
	}
	public AdjacencyList getAdjacencyList() {
		return adjacencyList;
	}
	
	
}
