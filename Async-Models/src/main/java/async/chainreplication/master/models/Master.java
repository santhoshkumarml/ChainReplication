package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Master.
 */
public class Master implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3255536825088625386L;
	
	/** The master host. */
	String masterHost;
	
	/** The master port. */
	int masterPort;
	
	/** The master name. */
	String masterName;
	
	/** The heartbeat timeout. */
	long heartbeatTimeout = 5000;

	/**
	 * Instantiates a new master.
	 *
	 * @param masterHost the master host
	 * @param masterPort the master port
	 * @param masterName the master name
	 */
	public Master(String masterHost, int masterPort, String masterName) {
		this.masterHost = masterHost;
		this.masterPort = masterPort;
		this.masterName = masterName;
	}

	/**
	 * Gets the heartbeat timeout.
	 *
	 * @return the heartbeat timeout
	 */
	public long getHeartbeatTimeout() {
		return heartbeatTimeout;
	}

	/**
	 * Gets the master host.
	 *
	 * @return the master host
	 */
	public String getMasterHost() {
		return masterHost;
	}

	/**
	 * Gets the master name.
	 *
	 * @return the master name
	 */
	public String getMasterName() {
		return masterName;
	}

	/**
	 * Gets the master port.
	 *
	 * @return the master port
	 */
	public int getMasterPort() {
		return masterPort;
	}

	/**
	 * Sets the heartbeat timeout.
	 *
	 * @param heartbeatTimeout the new heartbeat timeout
	 */
	public void setHeartbeatTimeout(long heartbeatTimeout) {
		this.heartbeatTimeout = heartbeatTimeout;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Master [masterHost=" + masterHost + ", masterPort="
				+ masterPort + ", masterName=" + masterName + "]";
	}
}
