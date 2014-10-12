package async.chainreplication.communication.message.models;

import java.io.Serializable;

import async.chainreplication.master.models.Server;

public class MasterBankChangeMessage extends ChainReplicationMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;
	
	Server server;

	public MasterBankChangeMessage(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
