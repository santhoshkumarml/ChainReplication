package async.chainreplication.client.server.communication.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Reply.
 */
public class Reply implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6844641999813461387L;

	/** The req id. */
	String reqID;

	/**
	 * Gets the req id.
	 *
	 * @return the req id
	 */
	public String getReqID() {
		return reqID;
	}

	/**
	 * Sets the req id.
	 *
	 * @param reqID
	 *            the new req id
	 */
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Reply [reqID=" + reqID + "]";
	}
}
