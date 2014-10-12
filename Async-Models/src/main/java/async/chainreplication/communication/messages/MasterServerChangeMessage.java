package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Chain;

public class MasterServerChangeMessage extends MasterMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;
	
	Chain bank;
	
	public MasterServerChangeMessage(Chain bank) {
		this.bank = bank;
	}
	
	public Chain getBank() {
		return bank;
	}

}
