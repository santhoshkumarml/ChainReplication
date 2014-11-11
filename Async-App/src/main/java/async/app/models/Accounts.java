package async.app.models;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class Accounts.
 */
public class Accounts {

	/** The account num to account snap shot. */
	Map<Integer, AccountSnapshot> accountNumToAccountSnapShot = new HashMap<Integer, AccountSnapshot>();

	/**
	 * Adds the account.
	 *
	 * @param accountNum the account num
	 * @return the account snapshot
	 */
	public AccountSnapshot addAccount(int accountNum) {
		AccountSnapshot accountSnapShot;
		synchronized (accountNumToAccountSnapShot) {
			accountSnapShot = new AccountSnapshot(accountNum, 0);
			accountNumToAccountSnapShot.put(accountNum, accountSnapShot);
			return accountSnapShot;
		}
	}

	/**
	 * Gets the account snapshot.
	 *
	 * @param accountNum the account num
	 * @return the account snapshot
	 */
	public AccountSnapshot getAccountSnapshot(Integer accountNum) {
		synchronized (accountNumToAccountSnapShot) {
			return accountNumToAccountSnapShot.get(accountNum);
		}
	}
	
	
	
	private Map<Integer, AccountSnapshot> getAccountNumToAccountSnapShot() {
		return accountNumToAccountSnapShot;
	}
	

	public Accounts copy(Accounts accounts) {
		Accounts newAccounts = new Accounts();
		for (Map.Entry<Integer, AccountSnapshot> accountEntry : 
			accounts.getAccountNumToAccountSnapShot().entrySet()) {
			newAccounts.getAccountNumToAccountSnapShot().put(
					accountEntry.getKey(),
					new AccountSnapshot(
							accountEntry.getKey(),
							accountEntry.getValue().getBalance()));
		}
		return newAccounts;
	}
}
