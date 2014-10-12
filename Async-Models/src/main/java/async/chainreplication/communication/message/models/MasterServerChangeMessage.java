package async.chainreplication.communication.message.models;

import async.chainreplication.application.models.Bank;

public class MasterServerChangeMessage extends ChainReplicationMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;
	
	Bank bank;
	
	public MasterServerChangeMessage(Bank bank) {
		this.bank = bank;
	}
	
	public Bank getBank() {
		return bank;
	}

}
