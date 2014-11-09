package async.chainreplication.server.models;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import async.chainreplication.client.server.communication.models.RequestKey;

public class SentHistory {
	//TODO: Add support for comparator so as to remove all 
	//less sequence number messages when ACKED
	//TODO: Enhance for Transfer
	int sequenceNumber = 0;
	Map<Integer, RequestKey> requestKeys = new TreeMap<Integer, RequestKey>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			return o1.compareTo(o2);
		}
	});
		
	public void addToSentHistory(RequestKey requestKey) {
		synchronized (this.requestKeys) {
			if(requestKey.getSequenceNumber() == 0) {
				requestKey.setSequenceNumber(++sequenceNumber);
			}
			requestKeys.put(requestKey.getSequenceNumber(), requestKey);
		}
	}
	
	public void removeFromSent(RequestKey requestKey) {
		synchronized (requestKeys) {
			for(int i=1;i<=requestKey.getSequenceNumber();i++) {
				this.requestKeys.remove(i);
			}
		}
	}
	
	public void removeFromSent(int sequenceNumber) {
		synchronized (requestKeys) {
			for(int i=1;i<=sequenceNumber;i++) {
				this.requestKeys.remove(i);	
			}
		}
	}
}
