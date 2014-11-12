package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 */
public class Client implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1866788520095538461L;

	/** The client id. */
	String clientId;

	/** The response wait time. */
	long responseWaitTime;

	/** The client process details. */
	ProcessDetails clientProcessDetails;

	/**
	 * Instantiates a new client.
	 *
	 * @param clientId
	 *            the client id
	 * @param host
	 *            the host
	 * @param responseWaitTime
	 *            the response wait time
	 */
	public Client(String clientId, String host, long responseWaitTime) {
		this.clientId = clientId;
		clientProcessDetails = new ProcessDetails();
		clientProcessDetails.setHost(host);
		this.responseWaitTime = responseWaitTime;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		final Client other = (Client) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (clientProcessDetails == null) {
			if (other.clientProcessDetails != null)
				return false;
		} else if (!clientProcessDetails.equals(other.clientProcessDetails))
			return false;
		return true;
	}

	/**
	 * Gets the client id.
	 *
	 * @return the client id
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Gets the client process details.
	 *
	 * @return the client process details
	 */
	public ProcessDetails getClientProcessDetails() {
		return clientProcessDetails;
	}

	/**
	 * Gets the response wait time.
	 *
	 * @return the response wait time
	 */
	public long getResponseWaitTime() {
		return responseWaitTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime
				* result
				+ ((clientProcessDetails == null) ? 0 : clientProcessDetails
						.hashCode());
		return result;
	}

	/**
	 * Sets the client id.
	 *
	 * @param clientId
	 *            the new client id
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * Sets the client process details.
	 *
	 * @param clientProcessDetails
	 *            the new client process details
	 */
	public void setClientProcessDetails(ProcessDetails clientProcessDetails) {
		this.clientProcessDetails = clientProcessDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", clientProcessDetails="
				+ clientProcessDetails.toString() + "]";
	}

}
