package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

import async.chainreplication.master.models.Client;

public class Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2145253058392283227L;
	String requestId;
	RequestType requestType;
	int retryCount = 0;
	Client client;

	public Request(Client client, String requestId) {
		//requestId = UUID.randomUUID().toString();
		this.client = client; 
		this.requestId =  requestId;
	}

	public int getRetryCount() {
		return retryCount;
	}



	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
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

	public RequestType getRequestType() {
		return requestType;
	}
	
	protected void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", requestType="
				+ requestType + ", client=" + client.getClientId() + "]";
	}

}
