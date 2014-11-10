package async.generic.message.queue;

public class Message<T> {
	long timestamp;
	T messageObject;
	int priority;

	/**
	 * Constructor
	 * 
	 * @param timestamp
	 * @param messageObject
	 * @param priority
	 */
	public Message(long timestamp, T messageObject, int priority) {
		this.timestamp = timestamp;
		this.messageObject = messageObject;
		this.priority = priority;
	}

	/**
	 * Copy constructor
	 *
	 * @param message
	 */
	public Message(Message<T> message) {
		this.timestamp = message.getTimestamp();
		this.priority = message.getPriority();
		this.messageObject = message.getMessageObject();
	}

	public T getMessageObject() {
		return messageObject;
	}

	public int getPriority() {
		return priority;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "Message [timestamp=" + timestamp + ", messageObject="
				+ messageObject + ", priority=" + priority + "]";
	}

}
