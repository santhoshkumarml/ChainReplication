package async.chainreplication.server.client.models;

import java.io.Serializable;

import async.server.client.model.exceptions.InvalidRequestException;


public class RequestDetails implements Serializable{
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
		if(requestType != RequestType.WITHDRAW && requestType != RequestType.DEPOSIT) {
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

}
