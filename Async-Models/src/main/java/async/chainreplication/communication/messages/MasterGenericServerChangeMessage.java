package async.chainreplication.communication.messages;

import java.util.ArrayList;
import java.util.List;

import async.chainreplication.master.models.Server;

public class MasterGenericServerChangeMessage extends MasterMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3939202406791902228L;
	
	List<Server> diedServers = new ArrayList<Server>();

	public MasterGenericServerChangeMessage(List<Server> diedServers) {
		this.diedServers.addAll(diedServers);
	}

	public List<Server> getDiedServers() {
		return diedServers;
	}
	
}
