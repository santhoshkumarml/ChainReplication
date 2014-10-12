package async.chainreplication.master.models;

import java.io.Serializable;


public class Chain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1094492046636943340L;
	String chainName;
	Server head;
	Server tail;
	public Chain(String bankName, Server head, Server tail) {
		this.chainName = bankName;
		this.head = head;
		this.tail = tail;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	public Server getHead() {
		return head;
	}
	public void setHead(Server head) {
		this.head = head;
	}
	public Server getTail() {
		return tail;
	}
	public void setTail(Server tail) {
		this.tail = tail;
	}
	
	
}
