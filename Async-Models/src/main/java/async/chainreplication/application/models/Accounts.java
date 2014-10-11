package async.chainreplication.application.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import async.chainreplication.server.models.IApplicationModel;
import async.chainreplication.server.models.IApplicationModels;

public class Accounts implements IApplicationModels<Integer,AccountSnapshot>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4123040867624640942L;
	
	Map<Integer, AccountSnapshot> accountNumToaccountSnapShot = 
			new HashMap<Integer, AccountSnapshot>();

	@Override
	public void addNew(IApplicationModel model) {
		synchronized (accountNumToaccountSnapShot) {
			AccountSnapshot accountSnapshot = (AccountSnapshot)model;
			if(!accountNumToaccountSnapShot.containsKey(accountSnapshot)) {
				accountNumToaccountSnapShot.put(accountSnapshot.getAccountNum(), accountSnapshot);
			}
		}
	}

	@Override
	public IApplicationModel get(Integer accountNum) {
		synchronized (accountNumToaccountSnapShot) {
			return this.accountNumToaccountSnapShot.get(accountNum);
		}
	}
}
