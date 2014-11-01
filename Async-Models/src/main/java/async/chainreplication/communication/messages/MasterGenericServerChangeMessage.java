package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import async.chainreplication.master.models.Server;

public class MasterGenericServerChangeMessage extends MasterMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3939202406791902228L;
	
	Set<Server> diedServers = new HashSet<Server>();

	public MasterGenericServerChangeMessage(List<Server> diedServers) {
		super(Priority.REALTIME_PRIORITY);
		this.diedServers.addAll(diedServers);
	}

	public Set<Server> getDiedServers() {
		return diedServers;
	}
	
}
