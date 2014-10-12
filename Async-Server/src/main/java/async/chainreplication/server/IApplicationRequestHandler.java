package async.chainreplication.server;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

public interface IApplicationRequestHandler {
	Reply handleRequest(Request request);

	void handleSyncUpdate(Reply reply);

}
