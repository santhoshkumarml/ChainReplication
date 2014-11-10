package async.chainreplication.communication.messages;

// TODO: Auto-generated Javadoc
/**
 * The Class WaitServerMessage.
 */
public class WaitServerMessage extends ChainReplicationMessage{

	/** The waiting class. */
	Class<?> waitingClass;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1371867708397244804L;
	
	/**
	 * Instantiates a new wait server message.
	 *
	 * @param waitingClass the waiting class
	 */
	public WaitServerMessage(Class<?> waitingClass) {
		super(Priority.REALTIME_PRIORITY);
		this.waitingClass = waitingClass;
	}

}
