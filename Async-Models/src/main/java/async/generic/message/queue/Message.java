package async.generic.message.queue;

public class Message<T> {
	long timestamp;
	T messageObject;
	int priority;
	
	public Message(long timestamp, T messageObject, int priority) {
		super();
		this.timestamp = timestamp;
		this.messageObject = messageObject;
		this.priority = priority;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public T getMessageObject() {
		return messageObject;
	}
	
	public int getPriority() {
		return priority;
	}
	
	@Override
	public String toString() {
		return "Message [timestamp=" + timestamp + ", messageObject="
				+ messageObject + ", priority=" + priority + "]";
	}
	
	
}
