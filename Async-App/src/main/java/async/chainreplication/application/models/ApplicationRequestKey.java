package async.chainreplication.application.models;

import async.chainreplication.client.server.communication.models.RequestKey;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationRequestKey.
 */
public class ApplicationRequestKey extends RequestKey {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -221866357836091925L;

	/** The account num. */
	int accountNum;

	/**
	 * Instantiates a new application request key.
	 *
	 * @param requestId the request id
	 * @param accountNum the account num
	 */
	public ApplicationRequestKey(String requestId, int accountNum) {
		super(requestId, 0);
		this.accountNum = accountNum;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.RequestKey#equals(java.lang.Object)
	 */
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

	/**
	 * Gets the account num.
	 *
	 * @return the account num
	 */
	public int getAccountNum() {
		return accountNum;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.RequestKey#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accountNum;
		return result;
	}

	/**
	 * Sets the account num.
	 *
	 * @param accountNum the new account num
	 */
	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.RequestKey#toString()
	 */
	@Override
	public String toString() {
		return "ApplicationRequestKey [accountNum=" + accountNum
				+ ", requestId=" + requestId + "]";
	}

}
