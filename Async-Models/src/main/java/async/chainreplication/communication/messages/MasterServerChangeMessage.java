package async.chainreplication.communication.messages;

import async.chainreplication.master.models.Server;

public class MasterServerChangeMessage extends MasterMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;
	
	Server server;
	
	public MasterServerChangeMessage(Server server) {
		this.server = server;
	}
	
	public Server getServer() {
		return this.server;
	}

}
