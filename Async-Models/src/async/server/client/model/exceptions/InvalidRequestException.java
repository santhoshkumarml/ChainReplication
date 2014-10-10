package async.server.client.model.exceptions;

import async.server.client.models.RequestDetails;

public class InvalidRequestException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(RequestDetails requestDetails) {
		super();
	}

}
