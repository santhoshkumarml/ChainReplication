package async.chainreplication.communication.messages;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerInitialiationCompleteMessage.
 */
public class ServerInitializationCompleteMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5673114903444266579L;

	/**
	 * Instantiates a new server initialiation complete message.
	 */
	public ServerInitializationCompleteMessage() {
		super(Priority.REALTIME_PRIORITY);
	}

}
