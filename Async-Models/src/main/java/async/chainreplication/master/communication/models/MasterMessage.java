package async.chainreplication.master.communication.models;

public class MasterMessage {
	public enum MasterMessageEnum {
		BANK_CHANGES,
		CHAIN_CHANGES //For Successor/Predecessor Change
	}

}
