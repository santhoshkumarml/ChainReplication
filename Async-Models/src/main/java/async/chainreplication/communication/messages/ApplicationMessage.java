package async.chainreplication.communication.messages;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationMessage.
 */
public class ApplicationMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1230980174250429713L;
	
	/** The transactional object. */
	Object transactionalObject;
	
	/** The is last message. */
	boolean isLastMessage;

	/**
	 * Instantiates a new application message.
	 *
	 * @param transactionalObject the transactional object
	 * @param isLastMessage the is last message
	 */
	public ApplicationMessage(Object transactionalObject, boolean isLastMessage) {
		super(Priority.HIGHER_PRIORITY);
		this.transactionalObject = transactionalObject;
		this.isLastMessage = isLastMessage;
	}

	/**
	 * Gets the transactional object.
	 *
	 * @return the transactional object
	 */
	public Object getTransactionalObject() {
		return transactionalObject;
	}

	/**
	 * Checks if is last message.
	 *
	 * @return true, if is last message
	 */
	public boolean isLastMessage() {
		return isLastMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String transactionalObjectString = null;
		if(transactionalObject!=null) {
			transactionalObjectString = transactionalObject.toString();
		}
		return "ApplicationMessage [transactionalObject=" + transactionalObjectString
				+ ", isLastMessage=" + isLastMessage + "]";
	}
	
	

}
