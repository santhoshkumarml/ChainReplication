package async.chainreplication.client;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

public interface IApplicationReplyHandler {
	public void handleResponse(Request request, Reply reply);
}
