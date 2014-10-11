package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

public class Reply implements Serializable{
	String reqID;
	Outcome outcome;
	float balance;
}
