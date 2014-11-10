package async.app.models;

import async.chainreplication.application.models.ApplicationReply;

// TODO: Auto-generated Javadoc
/**
 * The Class Response.
 */
public class Response {

	/** The request id. */
	String requestId;
	
	/** The application reply. */
	ApplicationReply applicationReply;

	/**
	 * Instantiates a new response.
	 *
	 * @param requestId the request id
	 * @param applicationReply the application reply
	 */
	public Response(String requestId, ApplicationReply applicationReply) {
		super();
		this.requestId = requestId;
		this.applicationReply = applicationReply;
	}

	/**
	 * Gets the application reply.
	 *
	 * @return the application reply
	 */
	public ApplicationReply getApplicationReply() {
		return applicationReply;
	}

	/**
	 * Gets the request id.
	 *
	 * @return the request id
	 */
	public String getRequestId() {
		return requestId;
	}

}
