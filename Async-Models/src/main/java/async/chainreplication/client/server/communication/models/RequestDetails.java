package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

import async.server.client.model.exceptions.InvalidRequestException;


public class RequestDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8844277582877496842L;
	RequestType requestType;
	int accountNum;
	int amount;
	String destBank;
	int destAccountNum;

	public RequestDetails(RequestType requestType, int accountNum, int amount,
			String destBank, int destAccountNum) throws InvalidRequestException {
		this.requestType = requestType;
		this.accountNum = accountNum;
		this.amount = amount;
		this.destBank = destBank;
		this.destAccountNum = destAccountNum;		
		if(requestType != RequestType.TRANSFER) {
			throw new InvalidRequestException(this);
		}
	}

	public RequestDetails(
			RequestType requestType,
			int accountNum) throws InvalidRequestException {
		this.requestType = requestType;
		this.accountNum = accountNum;
		if(requestType != RequestType.GET_BALANCE) {
			throw new InvalidRequestException(this);
		}
	}

	public RequestDetails(
			RequestType requestType, int accountNum,
			int amount) throws InvalidRequestException {
		this.requestType = requestType;
		this.accountNum = accountNum;
		this.amount = amount;
		if(requestType == RequestType.GET_BALANCE) {
			throw new InvalidRequestException(this);
		}
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public int getAccountNum() {
		return accountNum;
	}

	public int getAmount() {
		return amount;
	}

	public String getDestBank() {
		return destBank;
	}

	public int getDestAccountNum() {
		/*		if(requestType != RequestType.TRANSFER) {
			throw new InvalidOperationException("Should be called on a Transfer Request");
		}*/
		return destAccountNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + accountNum;
		if(requestType != RequestType.GET_BALANCE)
			result = prime * result + amount;
		if(requestType == RequestType.TRANSFER) {
			result = prime * result + destAccountNum;
			result = prime * result
					+ ((destBank == null) ? 0 : destBank.hashCode());
		}
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

		RequestDetails other = (RequestDetails) obj;
		if (requestType != other.requestType)
			return false;
		if (accountNum != other.accountNum)
			return false;
		if(requestType != RequestType.GET_BALANCE)
			if (amount != other.amount)
				return false;
		if(requestType == RequestType.TRANSFER) {
			if (destAccountNum != other.destAccountNum)
				return false;
			if (destBank == null) {
				if (other.destBank != null)
					return false;
			} else if (!destBank.equals(other.destBank))
				return false;
		}
		return true;
	}




}
