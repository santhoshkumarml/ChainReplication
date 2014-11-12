package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterServerChangeMessage.
 */
public class MasterChainJoinReplyMessage extends MasterMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7574520044298891698L;

	/** The server. */
	Server exisistingTail;

	/**
	 * Instantiates a new master server change message.
	 *
	 * @param exisistingTail
	 *            the exisisting tail
	 */
	public MasterChainJoinReplyMessage(Server exisistingTail) {
		super(Priority.REALTIME_PRIORITY);
		this.exisistingTail = exisistingTail;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getExisistingTail() {
		return exisistingTail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String exisistingTailString = null;
		if(exisistingTail != null) {
			exisistingTailString = exisistingTail.toString();
		}
		return "MasterChainJoinReplyMessage [exisistingTail=" + exisistingTailString
				+ "]";
	}

}
