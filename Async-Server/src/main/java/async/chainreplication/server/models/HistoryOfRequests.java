package async.chainreplication.server.models;

import java.util.HashMap;
import java.util.Map;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.server.communication.models.RequestKey;

public class HistoryOfRequests {
	Map<RequestKey, Reply> requestToReply = new HashMap<RequestKey, Reply>();
	Map<RequestKey, Request> requestKeyToRequest = new HashMap<RequestKey, Request>();

	public void addToHistory(RequestKey requestKey,Request request, Reply reply) {
		synchronized (requestToReply) {
			if(!isHistoryPresent(requestKey)) {
				requestToReply.put(requestKey, reply);
				requestKeyToRequest.put(requestKey, request);
			}	
		}
	}

	public boolean isHistoryPresent(RequestKey requestKey) {
		synchronized (requestToReply) {
			return requestToReply.containsKey(requestKey);
		}
	}
	
	public Request getExisistingRequest(RequestKey requestKey) {
		synchronized (requestToReply) {
			return this.requestKeyToRequest.get(requestKey);
		}
	}
	
	public Reply getExisistingReply(RequestKey requestKey) {
		synchronized (requestToReply) {
			return requestToReply.get(requestKey);
		}
	}

}
