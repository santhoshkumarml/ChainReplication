package async.chainreplication.server.models;

import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.server.communication.models.RequestKey;

// TODO: Auto-generated Javadoc
/**
 * The Class HistoryOfRequests.
 */
public class HistoryOfRequests {
	
	/** The request to reply. */
	Map<RequestKey, Reply> requestToReply = new HashMap<RequestKey, Reply>();
	
	/** The request key to request. */
	Map<RequestKey, Request> requestKeyToRequest = new HashMap<RequestKey, Request>();

	/**
	 * Adds the to history.
	 *
	 * @param requestKey the request key
	 * @param request the request
	 * @param reply the reply
	 */
	public void addToHistory(RequestKey requestKey, Request request, Reply reply) {
		synchronized (requestToReply) {
			if (!isHistoryPresent(requestKey)) {
				requestToReply.put(requestKey, reply);
				requestKeyToRequest.put(requestKey, request);
			}
		}
	}

	/**
	 * Gets the exisisting reply.
	 *
	 * @param requestKey the request key
	 * @return the exisisting reply
	 */
	public Reply getExisistingReply(RequestKey requestKey) {
		synchronized (requestToReply) {
			return requestToReply.get(requestKey);
		}
	}

	/**
	 * Gets the exisisting request.
	 *
	 * @param requestKey the request key
	 * @return the exisisting request
	 */
	public Request getExisistingRequest(RequestKey requestKey) {
		synchronized (requestToReply) {
			return requestKeyToRequest.get(requestKey);
		}
	}

	/**
	 * Checks if is history present.
	 *
	 * @param requestKey the request key
	 * @return true, if is history present
	 */
	public boolean isHistoryPresent(RequestKey requestKey) {
		synchronized (requestToReply) {
			return requestToReply.containsKey(requestKey);
		}
	}

}
