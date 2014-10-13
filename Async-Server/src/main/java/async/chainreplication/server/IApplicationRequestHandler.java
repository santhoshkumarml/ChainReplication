package async.chainreplication.server;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

public interface IApplicationRequestHandler {
	Reply handleRequest(Request request);

	void handleSyncUpdate(Request request,Reply reply);
	
	void updateHistories(Request request, Reply reply);
	
	void handleAck(Request request);

}
