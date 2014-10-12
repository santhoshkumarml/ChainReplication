package async.app.models;

import async.chainreplication.application.models.ApplicationReply;

public class Response {
	
	String requestId;
	ApplicationReply applicationReply;
	
	public Response(String requestId, ApplicationReply applicationReply) {
		super();
		this.requestId = requestId;
		this.applicationReply = applicationReply;
	}

	public String getRequestId() {
		return requestId;
	}

	public ApplicationReply getApplicationReply() {
		return applicationReply;
	}

}
