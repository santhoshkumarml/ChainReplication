package async.chainreplication.communication.messages;

import java.io.Serializable;

public class SuccessorRequestMessage extends ChainReplicationMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8305112073142789983L;
	
	int lastSequenceNumberReceived;

	public SuccessorRequestMessage(Priority priority, int lastSequenceNumberReceived) {
		super(priority);
		this.lastSequenceNumberReceived = lastSequenceNumberReceived;
	}

}
