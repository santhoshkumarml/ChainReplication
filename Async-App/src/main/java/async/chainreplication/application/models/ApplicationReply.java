package async.chainreplication.application.models;

import async.chainreplication.client.server.communication.models.Reply;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationReply.
 */
public class ApplicationReply extends Reply {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6447168651450761939L;

	/** The outcome. */
	Outcome outcome;
	
	/** The account num. */
	int accountNum;
	
	/** The balance. */
	float balance;

	/**
	 * Gets the account num.
	 *
	 * @return the account num
	 */
	public int getAccountNum() {
		return accountNum;
	}

	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	public float getBalance() {
		return balance;
	}

	/**
	 * Gets the outcome.
	 *
	 * @return the outcome
	 */
	public Outcome getOutcome() {
		return outcome;
	}

	/**
	 * Sets the account num.
	 *
	 * @param accountNum the new account num
	 */
	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	/**
	 * Sets the balance.
	 *
	 * @param balance the new balance
	 */
	public void setBalance(float balance) {
		this.balance = balance;
	}

	/**
	 * Sets the outcome.
	 *
	 * @param outcome the new outcome
	 */
	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.Reply#toString()
	 */
	@Override
	public String toString() {
		return "Reply[outcome=" + outcome + ", accountNum=" + accountNum
				+ ", balance=" + balance + ", reqID=" + getReqID() + "]";
	}

}
