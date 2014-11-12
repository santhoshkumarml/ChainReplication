package async.app.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	 * @param accountNum
	 *            the account num
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
	 * @param accountNum
	 *            the account num
	 * @return the account snapshot
	 */
	public AccountSnapshot getAccountSnapshot(Integer accountNum) {
		synchronized (accountNumToAccountSnapShot) {
			return accountNumToAccountSnapShot.get(accountNum);
		}
	}

	/**
	 * Copy.
	 *
	 * @return the accounts
	 */
	public Set<AccountSnapshot> getAccountSnapShots() {
		final Set<AccountSnapshot> accountSnapshots = new HashSet<AccountSnapshot>();
		synchronized (this) {
			for (final Map.Entry<Integer, AccountSnapshot> accountEntry : accountNumToAccountSnapShot
					.entrySet()) {
				accountSnapshots.add(new AccountSnapshot(accountEntry.getKey(),
						accountEntry.getValue().getBalance()));
			}
		}
		return accountSnapshots;
	}

	/**
	 * Update account snapshot.
	 *
	 * @param accountNum
	 *            the account num
	 * @param balance
	 *            the balance
	 */
	public void updateAccountSnapshot(Integer accountNum, float balance) {
		synchronized (accountNumToAccountSnapShot) {
			accountNumToAccountSnapShot.get(accountNum).setBalance(balance);
		}
	}
}
