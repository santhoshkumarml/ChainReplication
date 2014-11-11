package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainJoinMessage.
 */
public class ChainJoinMessage extends ChainReplicationMessage {
	
	/** The server. */
	Server server;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2971820276311529915L;

	/**
	 * Instantiates a new chain join message.
	 *
	 * @param server the server
	 */
	public ChainJoinMessage(Server server) {
		super(Priority.REALTIME_PRIORITY);
		this.server = server;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

}
