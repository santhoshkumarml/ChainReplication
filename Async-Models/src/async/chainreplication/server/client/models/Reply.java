package async.chainreplication.server.client.models;

import java.io.Serializable;

public class Reply implements Serializable{
	String reqID;
	Outcome outcome;
	float balance;
}
