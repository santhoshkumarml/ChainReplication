package async.message.facade;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.models.HistoryOfRequests;
import async.chainreplication.server.models.SentHistory;


public class ChainReplicationFacade {	

	Request currentRequest;
	Reply currentReply;
	SentHistory sentHistory;
	HistoryOfRequests historyOfRequests;
	
	
	public ChainReplicationFacade(Server server,Master master) {
		
	}
	
}
