package async.connection.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// TODO: Auto-generated Javadoc
/**
 * The Class UDPClientHelper.
 */
public class UDPClientHelper implements IClientHelper {

	/** The server host. */
	String serverHost;

	/** The port. */
	int port;

	/**
	 * Instantiates a new UDP client helper.
	 *
	 * @param serverHost
	 *            the server host
	 * @param port
	 *            the port
	 */
	public UDPClientHelper(String serverHost, int port) {
		this.serverHost = serverHost;
		this.port = port;
	}
	
	/* (non-Javadoc)
	 * @see async.connection.util.IClientHelper#getServerPort()
	 */
	@Override
	public int getServerPort() {
		return this.port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see async.connection.util.IClientHelper#sendMessage(java.lang.Object)
	 */
	@Override
	public void sendMessage(Object message) throws ConnectClientException {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
			byte[] data = null;
			ObjectOutputStream os = null;
			try {
				final ByteArrayOutputStream baos = new ByteArrayOutputStream(
						2048);
				os = new ObjectOutputStream(baos);
				os.writeObject(message);
				data = baos.toByteArray();
			} catch (final IOException e) {
				throw new ConnectClientException(e);
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				final DatagramPacket dp = new DatagramPacket(data, data.length,
						InetAddress.getByName(serverHost), port);
				clientSocket.send(dp);
			} catch (final IOException e) {
				throw new ConnectClientException(e);
			}
		} catch (final IOException e) {
			throw new ConnectClientException(e);
		} finally {
			if (clientSocket != null) {
				clientSocket.close();
			}
		}
	}
}