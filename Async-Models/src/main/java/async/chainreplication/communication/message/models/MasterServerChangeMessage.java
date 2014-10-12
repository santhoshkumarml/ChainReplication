package async.chainreplication.communication.message.models;

import java.io.Serializable;

import async.chainreplication.application.models.Bank;

public class MasterServerChangeMessage extends ChainReplicationMessage implements Serializable{
	
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
