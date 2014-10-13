package async.generic.message.queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class MessageQueue<T> {

	Queue<Message<T>> messages = new PriorityQueue<Message<T>>(new Comparator<Message<T>>() {
		@Override
		public int compare(Message<T> o1, Message<T> o2) {
			return Long.compare(o1.getTimestamp(), o2.getTimestamp());
		}
	});

	public Object dequeueMessage() {
		synchronized (messages) {
			return messages.remove().getMessageObject();
		}
	}

	public void enqueueMessage(T messageObject) {
		synchronized (messages) {
			messages.add(new Message<T>(System.currentTimeMillis(), messageObject));
		}
	}

	public boolean hasMoreMessages() {
		synchronized (messages) {
			return messages.size()>0;	
		}
	}

	@Override
	public String toString() {
		return "MessageQueue [messages=" + messages + "]";
	}
	
	

}
