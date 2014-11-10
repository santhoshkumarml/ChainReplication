package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import async.chainreplication.master.models.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterGenericServerChangeMessage.
 */
public class MasterGenericServerChangeMessage extends MasterMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3939202406791902228L;

	/** The died servers. */
	Set<Server> diedServers = new HashSet<Server>();

	/**
	 * Instantiates a new master generic server change message.
	 *
	 * @param diedServers the died servers
	 */
	public MasterGenericServerChangeMessage(List<Server> diedServers) {
		super(Priority.REALTIME_PRIORITY);
		this.diedServers.addAll(diedServers);
	}

	/**
	 * Gets the died servers.
	 *
	 * @return the died servers
	 */
	public Set<Server> getDiedServers() {
		return diedServers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MasterGenericServerChangeMessage [diedServers=" + diedServers
				+ "]";
	}

}
