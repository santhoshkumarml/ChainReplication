package async.chainreplication.server.models;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.client.server.communication.models.RequestKey;

public class SentHistory {
	//TODO: Add support for comparator so as to remove all 
	//less sequence number messages when ACKED
	//TODO: Enhance for Transfer
	Set<RequestKey> requestKeys = new HashSet<RequestKey>();
	public void addToSentHistory(RequestKey requestKey) {
		synchronized (this.requestKeys) {
			requestKeys.add(requestKey);
		}
	}
	public void removeFromSent(RequestKey requestKey) {
		synchronized (requestKeys) {
			this.requestKeys.remove(requestKey);
		}
	}

	public Set<String> getRequestIds() {
		return this.getRequestIds();
	}
}
