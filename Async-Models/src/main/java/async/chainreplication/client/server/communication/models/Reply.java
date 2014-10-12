package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

public class Reply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6844641999813461387L;
	
	String reqID;
	Outcome outcome;
	float balance;
	public String getReqID() {
		return reqID;
	}
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}
	public Outcome getOutcome() {
		return outcome;
	}
	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}	
}
