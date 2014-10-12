package async.server.client.model.exceptions;

import async.chainreplication.application.models.ApplicationRequest;

public class InvalidRequestException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(ApplicationRequest requestDetails) {
		super();
	}

}
