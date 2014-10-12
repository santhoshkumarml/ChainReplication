package async.chainreplication.app.client.handler;

import async.app.models.Response;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.client.IApplicationReplyHandler;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.generic.message.queue.models.MessageQueue;


public class ApplicationReplyHandler implements IApplicationReplyHandler {
	
	MessageQueue<Response> responseMessages = new MessageQueue<Response>();

	@Override
	public void handleResponse(Request request, Reply reply) {
		Response response = new Response(request.getRequestId(), (ApplicationReply)reply);
		responseMessages.enqueueMessage(response);
	}

	public MessageQueue<Response> getResponseMessages() {
		return responseMessages;
	}

}
