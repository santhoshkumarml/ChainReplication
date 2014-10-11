package async.master;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import async.chainreplication.master.models.Bank;
import async.chainreplication.master.models.Server;

public class MasterDataStructure {
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


	public List<Bank> getBankChanges(
			ConcurrentHashMap<String, Bank> bankNameToBankMapFromCurrentTimeStamp) {
		List<Bank> changedBanks = new ArrayList<Bank>();
		synchronized (this.getBankNameToBankMap()) {
			for(ConcurrentHashMap.Entry<String, Bank> bankEntry :
				bankNameToBankMapFromCurrentTimeStamp.entrySet()) {
				Bank bankEntryFromPreviousTimeStamp = 
						this.getBankNameToBankMap().get(bankEntry.getKey());
				Bank bankEntryFromCurrentTimeStamp = bankEntry.getValue();

				if(!bankEntryFromPreviousTimeStamp.getHead().getServerId().equals(
						bankEntryFromCurrentTimeStamp.getHead().getServerId()) ||
						!bankEntryFromPreviousTimeStamp.getTail().getServerId().equals(
								bankEntryFromCurrentTimeStamp.getTail().getServerId())
						) {
					changedBanks.add(bankEntryFromCurrentTimeStamp);
				}
			}
			this.getBankNameToBankMap().clear();
			this.getBankNameToBankMap().putAll(bankNameToBankMapFromCurrentTimeStamp);
		}
		return changedBanks;
	}

}
