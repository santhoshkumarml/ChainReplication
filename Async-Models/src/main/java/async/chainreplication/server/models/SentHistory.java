package async.chainreplication.server.models;

import java.util.Set;
import java.util.TreeSet;

public class SentHistory {
	//TODO: Add support for comparator
	//TODO: Enhance for Transfer
	Set<String> requestIds = new TreeSet<String>();
	public void addToSentHistory(String requestId) {
		synchronized (this.requestIds) {
			requestIds.add(requestId);
		}
	}
	public void removeFromSent(String requestId) {
		synchronized (requestIds) {
			this.requestIds.remove(requestId);
		}
	}

	public Set<String> getRequestIds() {
		return this.getRequestIds();
	}
}
