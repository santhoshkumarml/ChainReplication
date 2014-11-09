package async.chainreplication.application.models;

import async.chainreplication.client.server.communication.models.RequestKey;

public class ApplicationRequestKey extends RequestKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = -221866357836091925L;
	
	int accountNum;

	public ApplicationRequestKey(String requestId, int accountNum) {
		super(requestId, 0);
		this.accountNum = accountNum;
	}

	public int getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	@Override
	public String toString() {
		return "ApplicationRequestKey [accountNum=" + accountNum
				+ ", requestId=" + requestId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accountNum;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationRequestKey other = (ApplicationRequestKey) obj;
		if (accountNum != other.accountNum)
			return false;
		return true;
	}
	
	

}
