package async.chainreplication.communication.messages;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

public class ResponseOrSyncMessage extends ChainReplicationMessage{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6544018697459760664L;
	Request request;
	Reply reply;
	public ResponseOrSyncMessage(Request request, Reply reply) {
		this.request = request;
		this.reply = reply;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public Reply getReply() {
		return reply;
	}
	public void setReply(Reply reply) {
		this.reply = reply;
	}
	

}
