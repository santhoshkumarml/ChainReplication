package async.chainreplication.client.server.communication.models;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable{
	String requestId;
	String clientId;

	RequestDetails requestDetails;

	public Request(String clientId, RequestDetails requestDetails) {
		requestId = UUID.randomUUID().toString();
		this.clientId = clientId; 
		this.requestDetails = requestDetails;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getClientId() {
		return clientId;
	}

	public RequestDetails getRequestDetails() {
		return requestDetails;
	}	
}
