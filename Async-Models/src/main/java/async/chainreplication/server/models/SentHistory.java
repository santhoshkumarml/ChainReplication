package async.chainreplication.server.models;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.client.server.communication.models.Request;

public class SentHistory {
	Set<Request> requestId = new HashSet<Request>();

}
