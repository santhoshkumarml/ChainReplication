package async.chainreplication.application.models;

import java.io.Serializable;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Client;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationRequest.
 */
public class ApplicationRequest extends Request implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8844277582877496842L;
	
	/** The application request type. */
	ApplicationRequestType applicationRequestType;
	
	/** The account num. */
	int accountNum;
	
	/** The amount. */
	int amount;
	
	/** The dest bank. */
	String destBank;
	
	/** The dest account num. */
	int destAccountNum;

	/**
	 * Instantiates a new application request.
	 *
	 * @param client the client
	 * @param requestId the request id
	 */
	public ApplicationRequest(Client client, String requestId) {
		super(client, requestId);
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.Request#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationRequest other = (ApplicationRequest) obj;
		if (accountNum != other.accountNum)
			return false;
		if (amount != other.amount)
			return false;
		if (destAccountNum != other.destAccountNum)
			return false;
		if (destBank == null) {
			if (other.destBank != null)
				return false;
		} else if (!destBank.equals(other.destBank))
			return false;
		if (applicationRequestType != other.applicationRequestType)
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

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the application request type.
	 *
	 * @return the application request type
	 */
	public ApplicationRequestType getApplicationRequestType() {
		return applicationRequestType;
	}

	/**
	 * Gets the dest account num.
	 *
	 * @return the dest account num
	 */
	public int getDestAccountNum() {
		return destAccountNum;
	}

	/**
	 * Gets the dest bank.
	 *
	 * @return the dest bank
	 */
	public String getDestBank() {
		return destBank;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.Request#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accountNum;
		result = prime * result + amount;
		result = prime * result + destAccountNum;
		result = prime * result
				+ ((destBank == null) ? 0 : destBank.hashCode());
		result = prime
				* result
				+ ((applicationRequestType == null) ? 0
						: applicationRequestType.hashCode());
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

	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Sets the application request type.
	 *
	 * @param applicationRequestType the new application request type
	 */
	public void setApplicationRequestType(
			ApplicationRequestType applicationRequestType) {
		this.applicationRequestType = applicationRequestType;
		this.setRequestType(applicationRequestType.getRequestType());
	}

	/**
	 * Sets the dest account num.
	 *
	 * @param destAccountNum the new dest account num
	 */
	public void setDestAccountNum(int destAccountNum) {
		this.destAccountNum = destAccountNum;
	}

	/**
	 * Sets the dest bank.
	 *
	 * @param destBank the new dest bank
	 */
	public void setDestBank(String destBank) {
		this.destBank = destBank;
	}

	/* (non-Javadoc)
	 * @see async.chainreplication.client.server.communication.models.Request#toString()
	 */
	@Override
	public String toString() {
		return "Request [requestType=" + applicationRequestType
				+ ", accountNum=" + accountNum + ", amount=" + amount
				+ ", destBank=" + destBank + ", destAccountNum="
				+ destAccountNum + ", requestId=" + getRequestId()
				+ ", requestType=" + getRequestType() + ", client="
				+ getClient().getClientId() + "]";
	}

}
