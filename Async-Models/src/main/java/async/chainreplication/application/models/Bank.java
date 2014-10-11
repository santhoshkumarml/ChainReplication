package async.chainreplication.application.models;

import java.io.Serializable;

import async.chainreplication.master.models.Server;


public class Bank implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1094492046636943340L;
	String bankName;
	Server head;
	Server tail;
	public Bank(String bankName, Server head, Server tail) {
		this.bankName = bankName;
		this.head = head;
		this.tail = tail;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
