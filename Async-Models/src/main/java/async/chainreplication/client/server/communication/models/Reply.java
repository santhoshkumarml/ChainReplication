package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

public class Reply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6844641999813461387L;
	
	String reqID;
	
	public String getReqID() {
		return reqID;
	}
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}
	
	@Override
	public String toString() {
		return "Reply [reqID=" + reqID + "]";
	}
}
