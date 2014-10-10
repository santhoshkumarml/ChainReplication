package async.chainreplication.server.client.models;

public class Request {
	String requestId;

	RequestDetails requestDetails;

	public Request(RequestDetails requestDetails) {
		this.requestDetails = requestDetails;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public RequestDetails getRequestDetails() {
		return requestDetails;
	}	
}
