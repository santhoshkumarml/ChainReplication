package async.chainreplication.server.models;

import java.util.HashMap;
import java.util.Map;

public class Accounts {

	Map<Integer, AccountSnapshot> accountNumToAccountSnapShot = 
			new HashMap<Integer, AccountSnapshot>();

	public AccountSnapshot getAccountSnapshot(Integer accountNum) {
		synchronized (accountNumToAccountSnapShot) {
			return accountNumToAccountSnapShot.get(accountNum);
		}
	}

	public void updateAccount(int accountNum, float balance) {
		synchronized (accountNumToAccountSnapShot) {
			getAccountSnapshot(accountNum).setBalance(balance);	
		}
	}

}
