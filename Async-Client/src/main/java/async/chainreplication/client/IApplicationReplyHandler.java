package async.chainreplication.client;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Interface IApplicationReplyHandler.
 */
public interface IApplicationReplyHandler {
	
	/**
	 * Gets the response for request id.
	 *
	 * @param request the request
	 * @return the response for request id
	 */
	public Reply getResponseForRequestId(Request request);

	/**
	 * Handle response.
	 *
	 * @param request the request
	 * @param reply the reply
	 */
	public void handleResponse(Request request, Reply reply);
}
