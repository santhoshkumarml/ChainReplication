package async.chainreplication.application.models;

import java.io.Serializable;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Client;


public class ApplicationRequest  extends Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8844277582877496842L;
	RequestType requestType;
	int accountNum;
	int amount;
	String destBank;
	int destAccountNum;

	public ApplicationRequest(Client client, String requestId) {
		super(client, requestId);
		// TODO Auto-generated constructor stub
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public int getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDestBank() {
		return destBank;
	}

	public void setDestBank(String destBank) {
		this.destBank = destBank;
	}

	public int getDestAccountNum() {
		return destAccountNum;
	}

	public void setDestAccountNum(int destAccountNum) {
		this.destAccountNum = destAccountNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accountNum;
		result = prime * result + amount;
		result = prime * result + destAccountNum;
		result = prime * result
				+ ((destBank == null) ? 0 : destBank.hashCode());
		result = prime * result
				+ ((requestType == null) ? 0 : requestType.hashCode());
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
		if (requestType != other.requestType)
			return false;
		return true;
	}
	
	

}
