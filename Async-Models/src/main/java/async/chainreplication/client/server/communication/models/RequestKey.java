package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestKey.
 */
public abstract class RequestKey implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3748661814040823614L;

	/** The request id. */
	protected String requestId;

	/** The sequence number. */
	protected int sequenceNumber = 0;

	/**
	 * Instantiates a new request key.
	 *
	 * @param requestId
	 *            the request id
	 * @param sequenceNumber
	 *            the sequence number
	 */
	public RequestKey(String requestId, int sequenceNumber) {
		this.requestId = requestId;
		this.sequenceNumber = sequenceNumber;
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
		final RequestKey other = (RequestKey) obj;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
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
	 * Gets the sequence number.
	 *
	 * @return the sequence number
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
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
				+ ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + sequenceNumber;
		return result;
	}

	/**
	 * Sets the request id.
	 *
	 * @param requestId
	 *            the new request id
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Sets the sequence number.
	 *
	 * @param sequenceNumber
	 *            the new sequence number
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RequestKey [requestId=" + requestId + "]";
	}

}
