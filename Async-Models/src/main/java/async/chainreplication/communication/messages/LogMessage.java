/**
 *
 */
package async.chainreplication.communication.messages;

// TODO: Auto-generated Javadoc
/**
 * The Class LogMessage.
 *
 * @author Santhosh Kumar Manavasi Lakshminarayanan
 */
public class LogMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8797873379311671762L;

	/** The message. */
	String message;

	/**
	 * Instantiates a new log message.
	 *
	 * @param message the message
	 */
	public LogMessage(String message) {
		super(Priority.NORMAL_PRIORITY);
		this.message = message;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LogMessage [message=" + message + "]";
	}

}
