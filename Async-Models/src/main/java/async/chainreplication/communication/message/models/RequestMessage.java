package async.chainreplication.communication.message.models;

import async.chainreplication.client.server.communication.models.Request;

public class RequestMessage extends ChainReplicationMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8953346890623222175L;
	
	Request request;

	public RequestMessage(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}
