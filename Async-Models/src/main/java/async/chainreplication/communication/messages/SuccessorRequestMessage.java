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

	/**
	 * Gets the last sequence number received.
	 *
	 * @return the last sequence number received
	 */
	public int getLastSequenceNumberReceived() {
		return lastSequenceNumberReceived;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SuccessorRequestMessage [lastSequenceNumberReceived="
				+ lastSequenceNumberReceived + "]";
	}

}
