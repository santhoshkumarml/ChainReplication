package async.chainreplication.master.models;

import java.io.Serializable;

public class Master implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3255536825088625386L;
	String masterHost;
	int masterPort;
	String masterName;
	long heartbeatTimeout = 5000;
	
	public Master(String masterHost, int masterPort, String masterName) {
		this.masterHost = masterHost;
		this.masterPort = masterPort;
		this.masterName = masterName;
	}
	public String getMasterHost() {
		return masterHost;
	}
	public int getMasterPort() {
		return masterPort;
	}
	
	public String getMasterName() {
		return masterName;
	}
	
	public long getHeartbeatTimeout() {
		return heartbeatTimeout;
	}
	public void setHeartbeatTimeout(long heartbeatTimeout) {
		this.heartbeatTimeout = heartbeatTimeout;
	}
	@Override
	public String toString() {
		return "Master [masterHost=" + masterHost + ", masterPort="
				+ masterPort + ", masterName=" + masterName + "]";
	}
}
