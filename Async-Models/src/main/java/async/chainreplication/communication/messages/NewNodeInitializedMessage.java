package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class NewNodeInitializedMessage.
 */
public class NewNodeInitializedMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1336965646983515465L;

	/** The server. */
	Server server;

	/**
	 * Instantiates a new new node initialized message.
	 *
	 * @param server
	 *            the server
	 */
	public NewNodeInitializedMessage(Server server) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NewNodeInitializedMessage [server=" + server.toString() + "]";
	}

}
