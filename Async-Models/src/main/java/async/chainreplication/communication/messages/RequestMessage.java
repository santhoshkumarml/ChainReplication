package async.chainreplication.communication.messages;

import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestMessage.
 */
public class RequestMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8953346890623222175L;

	/** The request. */
	Request request;

	/**
	 * Instantiates a new request message.
	 *
	 * @param request
	 *            the request
	 */
	public RequestMessage(Request request) {
		super(Priority.HIGH_PRIORITY);
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

	/**
	 * Sets the request.
	 *
	 * @param request
	 *            the new request
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RequestMessage [" + request.toString() + "]";
	}

}
