package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Server;

/**
 * The Class ChainJoinMessage.
 */
public class ChainJoinMessage extends ChainReplicationMessage {
	
	Server server;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2971820276311529915L;

	/**
	 * Instantiates a new chain join message.
	 */
	public ChainJoinMessage(Server server) {
		super(Priority.REALTIME_PRIORITY);
		this.server = server;
	}

}
