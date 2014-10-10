package async.server.client.model.exceptions;

import async.chainreplication.server.client.models.RequestDetails;

public class InvalidRequestException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(RequestDetails requestDetails) {
		super();
	}

}
