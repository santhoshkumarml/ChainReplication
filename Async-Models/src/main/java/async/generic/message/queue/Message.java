package async.generic.message.queue;

public class Message<T> {
	long timestamp;
	T messageObject;
	public Message(long timestamp, T messageObject) {
		super();
		this.timestamp = timestamp;
		this.messageObject = messageObject;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public T getMessageObject() {
		return messageObject;
	}
}
