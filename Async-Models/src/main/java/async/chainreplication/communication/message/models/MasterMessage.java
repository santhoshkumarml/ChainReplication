package async.chainreplication.communication.message.models;

public class MasterMessage {
	public enum MasterMessageEnum {
		BANK_CHANGES, // For Update on Head/Tail changes of banks
		CHAIN_CHANGES; //For Successor/Predecessor Change
	}

}
