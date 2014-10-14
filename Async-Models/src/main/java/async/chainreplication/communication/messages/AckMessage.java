package async.chainreplication.communication.messages;

import async.chainreplication.client.server.communication.models.Request;

public class AckMessage extends ChainReplicationMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7114891688432604776L;
	Request request;

	public Request getRequest() {
		return request;
	}

	public AckMessage(Request request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return "Ack["+ request.toString() + "]";
	}
	
}
