package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

import async.chainreplication.master.models.Client;

// TODO: Auto-generated Javadoc
/**
 * The Class Request.
 */
public class Request implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2145253058392283227L;
	
	/** The request id. */
	String requestId;
	
	/** The request type. */
	RequestType requestType;
	
	/** The retry count. */
	int retryCount = 0;
	
	/** The client. */
	Client client;

	/**
	 * Instantiates a new request.
	 *
	 * @param client the client
	 * @param requestId the request id
	 */
	public Request(Client client, String requestId) {
		// requestId = UUID.randomUUID().toString();
		this.client = client;
		this.requestId = requestId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Gets the request id.
	 *
	 * @return the request id
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Gets the request type.
	 *
	 * @return the request type
	 */
	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * Gets the retry count.
	 *
	 * @return the retry count
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		return result;
	}

	/**
	 * Sets the request id.
	 *
	 * @param requestId the new request id
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Sets the request type.
	 *
	 * @param requestType the new request type
	 */
	protected void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * Sets the retry count.
	 *
	 * @param retryCount the new retry count
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", requestType="
				+ requestType + ", client=" + client.getClientId() + "]";
	}

}
