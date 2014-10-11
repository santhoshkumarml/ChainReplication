package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

public class Reply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6844641999813461387L;
	String reqID;
	Outcome outcome;
	float balance;
}
