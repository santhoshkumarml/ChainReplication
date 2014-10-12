package async.app.models;

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

	public AccountSnapshot addAccount(int accountNum) {
		AccountSnapshot accountSnapShot;
		synchronized (accountNumToAccountSnapShot) {
			accountSnapShot = new AccountSnapshot(accountNum, 0);
			accountNumToAccountSnapShot.put(accountNum, accountSnapShot);
			return accountSnapShot;
		}
	}
}
