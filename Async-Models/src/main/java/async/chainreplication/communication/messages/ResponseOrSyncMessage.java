package async.chainreplication.communication.messages;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseOrSyncMessage.
 */
public class ResponseOrSyncMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6544018697459760664L;

	/** The request. */
	Request request;

	/** The reply. */
	Reply reply;

	/**
	 * Instantiates a new response or sync message.
	 *
	 * @param request
	 *            the request
	 * @param reply
	 *            the reply
	 */
	public ResponseOrSyncMessage(Request request, Reply reply) {
		super(Priority.HIGH_PRIORITY);
		this.request = request;
		this.reply = reply;
	}

	/**
	 * Gets the reply.
	 *
	 * @return the reply
	 */
	public Reply getReply() {
		return reply;
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
	 * Sets the reply.
	 *
	 * @param reply
	 *            the new reply
	 */
	public void setReply(Reply reply) {
		this.reply = reply;
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
		return "[" + request.toString() + "],[" + reply.toString() + "]";
	}

}
