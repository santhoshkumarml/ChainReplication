package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterServerChangeMessage.
 */
public class MasterServerChangeMessage extends MasterMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1891843269349186453L;

	/** The server. */
	Server server;

	/** The other chains. */
	Set<Chain> otherChains = new HashSet<Chain>();

	/**
	 * Instantiates a new master server change message.
	 *
	 * @param server the server
	 */
	public MasterServerChangeMessage(Server server) {
		super(Priority.REALTIME_PRIORITY);
		this.server = server;
	}

	/**
	 * Gets the other chains.
	 *
	 * @return the other chains
	 */
	public Set<Chain> getOtherChains() {
		assert server.isHead() || server.isTail();
		return otherChains;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MasterServerChangeMessage [server=" + server + ", otherChains="
				+ otherChains + "]";
	}

}
