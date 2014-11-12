package async.chainreplication.communication.messages;

import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class AckMessage.
 */
public class AckMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7114891688432604776L;

	/** The request. */
	Request request;

	/**
	 * Instantiates a new ack message.
	 *
	 * @param request
	 *            the request
	 */
	public AckMessage(Request request) {
		super(Priority.ABOVE_NORMAL_PRIORITY);
		this.request = request;
	}

	/**
	 * Gets the request.
	 *
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ack[" + request.toString() + "]";
	}

}
