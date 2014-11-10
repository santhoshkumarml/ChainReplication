package async.generic.message.queue;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 *
 * @param <T> the generic type
 */
public class Message<T> {
	
	/** The timestamp. */
	long timestamp;
	
	/** The message object. */
	T messageObject;
	
	/** The priority. */
	int priority;

	/**
	 * Constructor.
	 *
	 * @param timestamp the timestamp
	 * @param messageObject the message object
	 * @param priority the priority
	 */
	public Message(long timestamp, T messageObject, int priority) {
		this.timestamp = timestamp;
		this.messageObject = messageObject;
		this.priority = priority;
	}

	/**
	 * Copy constructor.
	 *
	 * @param message the message
	 */
	public Message(Message<T> message) {
		this.timestamp = message.getTimestamp();
		this.priority = message.getPriority();
		this.messageObject = message.getMessageObject();
	}

	/**
	 * Gets the message object.
	 *
	 * @return the message object
	 */
	public T getMessageObject() {
		return messageObject;
	}

	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [timestamp=" + timestamp + ", messageObject="
				+ messageObject + ", priority=" + priority + "]";
	}

}
