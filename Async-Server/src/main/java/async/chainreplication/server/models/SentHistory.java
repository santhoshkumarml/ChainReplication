package async.chainreplication.server.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import async.chainreplication.client.server.communication.models.RequestKey;

// TODO: Auto-generated Javadoc
/**
 * The Class SentHistory.
 */
public class SentHistory {
	// TODO: Add support for comparator so as to remove all
	// less sequence number messages when ACKED
	// TODO: Enhance for Transfer
	/** The sequence number. */
	int sequenceNumber = 0;

	/** The request keys. */
	TreeMap<Integer, RequestKey> requestKeys = new TreeMap<Integer, RequestKey>(
			new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					// TODO Auto-generated method stub
					return o1.compareTo(o2);
				}
			});

	/**
	 * Adds the to sent history.
	 *
	 * @param requestKey the request key
	 */
	public void addToSentHistory(RequestKey requestKey) {
		synchronized (requestKeys) {
			if (requestKey.getSequenceNumber() == 0) {
				requestKey.setSequenceNumber(++sequenceNumber);
			}
			requestKeys.put(requestKey.getSequenceNumber(), requestKey);
		}
	}

	/**
	 * Removes the from sent.
	 *
	 * @param sequenceNumber the sequence number
	 */
	public void removeFromSent(int sequenceNumber) {
		synchronized (requestKeys) {
			for (int i = 1; i <= sequenceNumber; i++) {
				requestKeys.remove(i);
			}
		}
	}

	/**
	 * Removes the from sent.
	 *
	 * @param requestKey the request key
	 */
	public void removeFromSent(RequestKey requestKey) {
		synchronized (requestKeys) {
			for (int i = 1; i <= requestKey.getSequenceNumber(); i++) {
				requestKeys.remove(i);
			}
		}
	}

	/**
	 * Gets the request keys from sent.
	 *
	 * @param sequenceNumber the sequence number
	 * @return the request keys from sent
	 */
	public List<RequestKey> getRequestKeysFromSent(int sequenceNumber) {
		List<RequestKey> matchingRequestKeys = new ArrayList<RequestKey>();
		synchronized (requestKeys) {
			int lastKey = this.requestKeys.lastKey();
			for(int i=sequenceNumber;i<=lastKey;i++) {
				matchingRequestKeys.add(this.requestKeys.get(i));
			}
		}
		return matchingRequestKeys;
	}
}
