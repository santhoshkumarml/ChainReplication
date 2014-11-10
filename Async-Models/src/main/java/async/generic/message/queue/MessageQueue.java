package async.generic.message.queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageQueue.
 *
 * @param <T> the generic type
 */
public class MessageQueue<T> {

	/** The messages. */
	Queue<Message<T>> messages = new PriorityQueue<Message<T>>(
			new Comparator<Message<T>>() {
				@Override
				public int compare(Message<T> o1, Message<T> o2) {
					int priorityCompare = new Integer(o1.getPriority())
							.compareTo(o2.getPriority());
					if (priorityCompare == 0)
						return Long.compare(o1.getTimestamp(),
								o2.getTimestamp());
					else
						return priorityCompare;
				}
			});

	/**
	 * Dequeue message.
	 *
	 * @return the message
	 */
	public Message<T> dequeueMessage() {
		synchronized (messages) {
			return messages.remove();
		}
	}

	/**
	 * Dequeue message and return message object.
	 *
	 * @return the object
	 */
	public Object dequeueMessageAndReturnMessageObject() {
		synchronized (messages) {
			return messages.remove().getMessageObject();
		}
	}

	/**
	 * Enqueue message object.
	 *
	 * @param pritority the pritority
	 * @param messageObject the message object
	 */
	public void enqueueMessageObject(int pritority, T messageObject) {
		synchronized (messages) {
			messages.add(new Message<T>(System.currentTimeMillis(),
					messageObject, pritority));
		}
	}

	/**
	 * Checks for more messages.
	 *
	 * @return true, if successful
	 */
	public boolean hasMoreMessages() {
		synchronized (messages) {
			return messages.size() > 0;
		}
	}

	/**
	 * Peek at message.
	 *
	 * @return the message
	 */
	public Message<T> peekAtMessage() {
		Message<T> message = messages.peek();
		if(message != null) {
			return new Message<T>(message);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageQueue [messages=" + messages + "]";
	}

}
