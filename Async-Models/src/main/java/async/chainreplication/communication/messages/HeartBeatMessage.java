package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class HeartBeatMessage.
 */
public class HeartBeatMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3257375233167205678L;

	/** The server. */
	Server server;

	/**
	 * Instantiates a new heart beat message.
	 *
	 * @param server
	 *            the server
	 */
	public HeartBeatMessage(Server server) {
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
		return "HeartBeatMessage [server=" + server + "]";
	}

}
