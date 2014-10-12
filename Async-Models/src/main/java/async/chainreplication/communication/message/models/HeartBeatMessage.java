package async.chainreplication.communication.message.models;

import async.chainreplication.master.models.Server;

public class HeartBeatMessage extends ChainReplicationMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3257375233167205678L;
	
	Server server;

	public HeartBeatMessage(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

}
