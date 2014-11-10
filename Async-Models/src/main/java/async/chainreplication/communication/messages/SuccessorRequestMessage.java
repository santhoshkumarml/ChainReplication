package async.chainreplication.communication.messages;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class SuccessorRequestMessage.
 */
public class SuccessorRequestMessage extends ChainReplicationMessage implements
		Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8305112073142789983L;

	/** The last sequence number received. */
	int lastSequenceNumberReceived;

	/**
	 * Instantiates a new successor request message.
	 *
	 * @param lastSequenceNumberReceived the last sequence number received
	 */
	public SuccessorRequestMessage(int lastSequenceNumberReceived) {
		super(Priority.HIGHER_PRIORITY);
		this.lastSequenceNumberReceived = lastSequenceNumberReceived;
	}

	public int getLastSequenceNumberReceived() {
		return lastSequenceNumberReceived;
	}

}
