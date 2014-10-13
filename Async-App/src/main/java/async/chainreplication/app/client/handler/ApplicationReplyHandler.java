package async.chainreplication.app.client.handler;

import async.app.models.Response;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.client.ClientMessageHandler;
import async.chainreplication.client.IApplicationReplyHandler;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.generic.message.queue.MessageQueue;


public class ApplicationReplyHandler implements IApplicationReplyHandler {
	
	MessageQueue<Response> responseMessages = new MessageQueue<Response>();
	ClientMessageHandler clientMessageHandler;
	
	public ApplicationReplyHandler(ClientMessageHandler clientMessageHandler) {
		this.clientMessageHandler = clientMessageHandler;
	}

	@Override
	public void handleResponse(Request request, Reply reply) {
		Response response = new Response(
				request.getRequestId(),
				(ApplicationReply)reply);
		responseMessages.enqueueMessageObject(response);
	}


	@Override
	public Reply getResponseForRequestId(Request request) {
		while(responseMessages.hasMoreMessages()) {
			Response response = (Response) responseMessages.dequeueMessageAndReturnMessageObject();
			if(response.getRequestId().equals(request.getRequestId())) {
				return response.getApplicationReply();
			}
		}
		return null;
	}

}
