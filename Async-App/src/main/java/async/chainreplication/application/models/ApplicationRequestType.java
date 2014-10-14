package async.chainreplication.application.models;

import async.chainreplication.client.server.communication.models.RequestType;

public enum ApplicationRequestType {
	GET_BALANCE(RequestType.QUERY),
	DEPOSIT(RequestType.UPDATE),
	WITHDRAW(RequestType.UPDATE),
	TRANSFER(RequestType.UPDATE);

	private RequestType requestType;

	ApplicationRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public RequestType getRequestType() {
		return requestType;
	}

}
