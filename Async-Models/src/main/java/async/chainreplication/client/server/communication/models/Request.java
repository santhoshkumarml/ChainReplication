package async.chainreplication.client.server.communication.models;

import java.io.Serializable;
import java.util.UUID;

import async.chainreplication.master.models.Client;

public class Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2145253058392283227L;
	String requestId;
	Client client;

	RequestDetails requestDetails;

	public Request(Client client, RequestDetails requestDetails) {
		requestId = UUID.randomUUID().toString();
		this.client = client; 
		this.requestDetails = requestDetails;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public Client getClient() {
		return client;
	}

	public RequestDetails getRequestDetails() {
		return requestDetails;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((client == null) ? 0 : client.hashCode());
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
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
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
