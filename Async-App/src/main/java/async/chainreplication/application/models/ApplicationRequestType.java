package async.chainreplication.application.models;

import async.chainreplication.client.server.communication.models.RequestType;

// TODO: Auto-generated Javadoc
/**
 * The Enum ApplicationRequestType.
 */
public enum ApplicationRequestType {

	/** The get balance. */
	GET_BALANCE(RequestType.QUERY),
	/** The deposit. */
	DEPOSIT(RequestType.UPDATE),
	/** The withdraw. */
	WITHDRAW(RequestType.UPDATE),
	/** The transfer. */
	TRANSFER(RequestType.UPDATE);

	/** The request type. */
	private RequestType requestType;

	/**
	 * Instantiates a new application request type.
	 *
	 * @param requestType
	 *            the request type
	 */
	ApplicationRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * Gets the request type.
	 *
	 * @return the request type
	 */
	public RequestType getRequestType() {
		return requestType;
	}

}
