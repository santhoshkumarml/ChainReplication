package async.server.client.model.exceptions;

import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidRequestException.
 */
public class InvalidRequestException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid request exception.
	 *
	 * @param request
	 *            the request
	 */
	public InvalidRequestException(Request request) {
		super();
	}

}
