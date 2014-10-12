package async.message.facade;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

public interface IServerRequestHandler {
	Reply handleRequest(Request request);

}
