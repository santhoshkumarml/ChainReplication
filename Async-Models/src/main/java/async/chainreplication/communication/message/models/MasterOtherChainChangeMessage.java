package async.chainreplication.communication.message.models;

import async.chainreplication.master.models.Server;

public class MasterOtherChainChangeMessage extends ChainReplicationMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;
	
	Server server;

	public MasterOtherChainChangeMessage(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
