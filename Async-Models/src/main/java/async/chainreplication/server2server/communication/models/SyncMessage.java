package async.chainreplication.server2server.communication.models;

import java.io.Serializable;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

public class SyncMessage implements Serializable{	
	Request request;
	Reply reply;

	public SyncMessage(Request request, Reply reply) {
		this.request = request;
		this.reply = reply;
	}

}
