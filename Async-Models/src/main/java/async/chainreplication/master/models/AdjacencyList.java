package async.chainreplication.master.models;

import java.io.Serializable;


public class AdjacencyList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1825439574320948280L;
	Server predecessor;
	Server sucessor;
	public AdjacencyList(Server predecessor, Server sucessor) {
		this.predecessor = predecessor;
		this.sucessor = sucessor;
	}
	public Server getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(Server predecessor) {
		this.predecessor = predecessor;
	}
	public Server getSucessor() {
		return sucessor;
	}
	public void setSucessor(Server sucessor) {
		this.sucessor = sucessor;
	}
	
}
