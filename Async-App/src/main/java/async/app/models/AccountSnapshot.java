package async.app.models;

import java.io.Serializable;

public class AccountSnapshot implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7489153312094528373L;
	int accountNum;
	float balance;
	public AccountSnapshot(int accountNum, float balance) {
		super();
		this.accountNum = accountNum;
		this.balance = balance;
	}
	public int getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	

}
