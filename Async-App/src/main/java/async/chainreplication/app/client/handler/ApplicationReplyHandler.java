package async.chainreplication.app.client.handler;

import async.app.models.Response;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.client.ClientMessageHandler;
import async.chainreplication.client.IApplicationReplyHandler;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.Priority;
import async.generic.message.queue.MessageQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationReplyHandler.
 */
public class ApplicationReplyHandler implements IApplicationReplyHandler {

	/** The response messages. */
	MessageQueue<Response> responseMessages = new MessageQueue<Response>();
	
	/** The client message handler. */
	ClientMessageHandler clientMessageHandler;

	/**
	 * Instantiates a new application reply handler.
	 *
	 * @param clientMessageHandler the client message handler
	 */
	public ApplicationReplyHandler(ClientMessageHandler clientMessageHandler) {
		this.clientMessageHandler = clientMessageHandler;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.IApplicationReplyHandler#getResponseForRequestId(async.chainreplication.client.server.communication.models.Request)
	 */
	@Override
	public Reply getResponseForRequestId(Request request) {
		while (responseMessages.hasMoreMessages()) {
			Response response = (Response) responseMessages
					.dequeueMessageAndReturnMessageObject();
			if (response.getRequestId().equals(request.getRequestId())) {
				return response.getApplicationReply();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.IApplicationReplyHandler#handleResponse(async.chainreplication.client.server.communication.models.Request, async.chainreplication.client.server.communication.models.Reply)
	 */
	@Override
	public void handleResponse(Request request, Reply reply) {
		Response response = new Response(request.getRequestId(),
				(ApplicationReply) reply);
		// All responses will have equal priority
		responseMessages.enqueueMessageObject(Priority.HIGH_PRIORITY.ordinal(),
				response);
	}

}
