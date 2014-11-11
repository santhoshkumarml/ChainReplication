package async.chainreplication.communication.messages;

public class ChainJoinMessage extends ChainReplicationMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971820276311529915L;

	public ChainJoinMessage() {
		super(Priority.REALTIME_PRIORITY);
	}

}
