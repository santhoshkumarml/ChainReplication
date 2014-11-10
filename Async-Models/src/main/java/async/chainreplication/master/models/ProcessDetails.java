package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
//Contain Process Details about each of the server
/**
 * The Class ProcessDetails.
 */
public class ProcessDetails implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -449386447593161029L;
	
	/** The host. */
	String host = "localhost";
	
	/** The tcp port. */
	int tcpPort = -1;
	
	/** The udp port. */
	int udpPort = -1;

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the tcp port.
	 *
	 * @return the tcp port
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * Gets the udp port.
	 *
	 * @return the udp port
	 */
	public int getUdpPort() {
		return udpPort;
	}

	/**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Sets the tcp port.
	 *
	 * @param tcpPort the new tcp port
	 */
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	/**
	 * Sets the udp port.
	 *
	 * @param udpPort the new udp port
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessDetails [host=" + host + ", tcpPort=" + tcpPort
				+ ", udpPort=" + udpPort + "]";
	}

}
