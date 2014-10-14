package async.chainreplication.master.models;

import java.io.Serializable;

public class Client implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1866788520095538461L;
	String clientId;
	long responseWaitTime;
	ProcessDetails clientProcessDetails;

	public Client(String clientId,String host, long responseWaitTime) {
		this.clientId = clientId;
		this.clientProcessDetails = new ProcessDetails();
		this.clientProcessDetails.setHost(host);
		this.responseWaitTime = responseWaitTime;
	}

	public long getResponseWaitTime() {
		return responseWaitTime;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public ProcessDetails getClientProcessDetails() {
		return clientProcessDetails;
	}

	public void setClientProcessDetails(ProcessDetails clientProcessDetails) {
		this.clientProcessDetails = clientProcessDetails;
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
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

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", clientProcessDetails="
				+ clientProcessDetails.toString() + "]";
	}
	
	
	

}
