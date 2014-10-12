package async.server.client.model.exceptions;

import async.chainreplication.client.server.communication.models.Request;

public class InvalidRequestException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(Request request) {
		super();
	}

}
