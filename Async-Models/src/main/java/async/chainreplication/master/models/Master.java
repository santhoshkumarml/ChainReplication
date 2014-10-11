package async.chainreplication.master.models;

public class Master {
	String masterHost;
	int masterPort;
	public Master(String masterHost, int masterPort) {
		this.masterHost = masterHost;
		this.masterPort = masterPort;
	}
	public String getMasterHost() {
		return masterHost;
	}
	public int getMasterPort() {
		return masterPort;
	}	
}
