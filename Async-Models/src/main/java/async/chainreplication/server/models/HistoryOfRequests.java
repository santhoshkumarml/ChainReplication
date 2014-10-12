package async.chainreplication.server.models;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.client.server.communication.models.Request;

public class HistoryOfRequests {
	Set<Request> requestSet = new HashSet<Request>();

	public void addToHistory(Request request) {
		synchronized (requestSet) {
			if(!isHistoryPresent(request)) {
				requestSet.add(request);		
			}	
		}
	}

	public boolean isHistoryPresent(Request request) {
		synchronized (requestSet) {
			return requestSet.contains(request);
		}
	}

}
