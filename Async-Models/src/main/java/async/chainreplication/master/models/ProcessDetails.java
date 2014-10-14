package async.chainreplication.master.models;

import java.io.Serializable;

//Contain Process Details about each of the server
public class ProcessDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -449386447593161029L;
	String host = "localhost";
	int tcpPort = -1;
	int udpPort = -1;


	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	@Override
	public String toString() {
		return "ProcessDetails [host=" + host + ", tcpPort="
				+ tcpPort + ", udpPort=" + udpPort + "]";
	}
	
}
