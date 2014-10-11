package async.chainreplication.client.server.communication.models;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2145253058392283227L;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result
				+ ((requestDetails == null) ? 0 : requestDetails.hashCode());
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (requestDetails == null) {
			if (other.requestDetails != null)
				return false;
		} else if (!requestDetails.equals(other.requestDetails))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		return true;
	}
	
}
