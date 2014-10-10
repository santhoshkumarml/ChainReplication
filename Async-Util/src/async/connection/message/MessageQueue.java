package async.connection.message;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class MessageQueue<T> {

	Queue<Message<T>> messages = new PriorityQueue(new Comparator<Message<T>>() {
		@Override
		public int compare(Message o1, Message o2) {
			return Long.compare(o1.getTimestamp(), o2.getTimestamp());
		}
	});

	public Object getNextMessage() {
		synchronized (messages) {
			return messages.remove().getMessageObject();
		}
	}

	public void pushMessage(Object messageObject) {
		synchronized (messages) {
			messages.add(new Message(System.currentTimeMillis(), messageObject));
		}
	}

	public boolean hasNextMessage() {
		synchronized (messages) {
			return messages.size()>0;	
		}
	}

}
