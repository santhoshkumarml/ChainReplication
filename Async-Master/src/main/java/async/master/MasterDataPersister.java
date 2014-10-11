package async.master;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import async.chainreplication.application.models.Bank;
import async.chainreplication.master.models.Server;

public class MasterDataPersister {
	ConcurrentHashMap<String, Bank> bankNameToBankMap = 
			new ConcurrentHashMap<String, Bank>();
	ConcurrentHashMap<String,TreeMap<String, Server>> bankToAllServersMap =
			new ConcurrentHashMap<String, TreeMap<String,Server>>();


	public ConcurrentHashMap<String, Bank> getBankNameToBankMap() {
		return bankNameToBankMap;
	}


	public ConcurrentHashMap<String, TreeMap<String, Server>> getBankToAllServersMap() {
		return bankToAllServersMap;
	}


	public void calculateChanges(
			ConcurrentHashMap<String,TreeMap<String, Server>> bankToAllServersMap) {
		List<Bank> changedBanks = new ArrayList<Bank>();
		synchronized (this.getBankNameToBankMap()) {

		}
		//return changedBanks;
	}

}
