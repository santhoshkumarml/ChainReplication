package async.chainreplication.server.models;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.client.server.communication.models.Request;

public class HistoryOfRequests {
	Set<Request> requestIdToRequestMap = new HashSet<Request>();

	public void addToHistory(Request request) {
		synchronized (requestIdToRequestMap) {
			if(!isHistoryPresent(request)) {
				requestIdToRequestMap.add(request);		
			}	
		}
	}

	public boolean isHistoryPresent(Request request) {
		synchronized (requestIdToRequestMap) {
			return requestIdToRequestMap.contains(request);
		}
	}

}
