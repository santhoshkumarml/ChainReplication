package async.chainreplication.application.models;

import async.chainreplication.client.server.communication.models.Reply;

public class ApplicationReply extends Reply {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6447168651450761939L;
	
	Outcome outcome;
	int accountNum;
	float balance;

	public int getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
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
	
	@Override
	public String toString() {
		return "ApplicationReply [outcome=" + outcome + ", accountNum="
				+ accountNum + ", balance=" + balance + ", reqID=" + getReqID()
				+ "]";
	}

}
