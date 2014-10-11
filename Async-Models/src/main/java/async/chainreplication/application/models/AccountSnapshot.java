package async.chainreplication.application.models;

import java.io.Serializable;

import async.chainreplication.server.models.IApplicationModel;

public class AccountSnapshot implements IApplicationModel<Integer,Float>, Serializable{
	
	int accountNum;
	float balance;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8744789367636188943L;

	@Override
	public void set(Integer key, Float value) {
		this.accountNum = key;
		this.balance = value;
	}

	public int getAccountNum() {
		return accountNum;
	}

	public float getBalance() {
		return balance;
	}
	
	

}
