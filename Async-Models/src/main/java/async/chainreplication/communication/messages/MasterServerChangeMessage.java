package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Server;

public class MasterServerChangeMessage extends MasterMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;

	Server server;

	Set<Chain> otherChains = new HashSet<Chain>();

	public MasterServerChangeMessage(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return this.server;
	}

	public Set<Chain> getOtherChains() {
		assert this.server.isHead() || this.server.isTail();
		return otherChains;
	}



}
