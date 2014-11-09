package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

public abstract class RequestKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3748661814040823614L;

	protected String requestId;
	
	protected int sequenceNumber = 0;

	public RequestKey(String requestId,int sequenceNumber) {
		this.requestId = requestId;
		this.sequenceNumber = sequenceNumber;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return "RequestKey [requestId=" + requestId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + sequenceNumber;
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
		RequestKey other = (RequestKey) obj;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}

	
}
