package async.app.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class AccountSnapshot.
 */
public class AccountSnapshot implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7489153312094528373L;
	
	/** The account num. */
	int accountNum;
	
	/** The balance. */
	float balance;

	/**
	 * Instantiates a new account snapshot.
	 *
	 * @param accountNum the account num
	 * @param balance the balance
	 */
	public AccountSnapshot(int accountNum, float balance) {
		super();
		this.accountNum = accountNum;
		this.balance = balance;
	}

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

}
