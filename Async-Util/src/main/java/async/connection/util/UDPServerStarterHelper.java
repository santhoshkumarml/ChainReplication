package async.connection.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// TODO: Auto-generated Javadoc
/**
 * The Class UDPServerStarterHelper.
 */
public class UDPServerStarterHelper implements IServerStarterHelper {
	
	/** The server socket. */
	DatagramSocket serverSocket;
	
	/** The port. */
	int port;

	/**
	 * Instantiates a new UDP server starter helper.
	 *
	 * @param port the port
	 */
	public UDPServerStarterHelper(int port) {
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see async.connection.util.IServerStarterHelper#acceptAndReadObjectConnection()
	 */
	@Override
	public Object acceptAndReadObjectConnection() throws ConnectServerException {
		Object message = null;
		byte[] receiveData = new byte[10240];
		ObjectInputStream oos = null;
		try {
			DatagramPacket packet = new DatagramPacket(receiveData,
					receiveData.length);
			serverSocket.receive(packet);
			ByteArrayInputStream baos = new ByteArrayInputStream(receiveData);
			oos = new ObjectInputStream(baos);
			message = oos.readObject();
		} catch (IOException e) {
			throw new ConnectServerException(e);
		} catch (ClassNotFoundException e) {
			throw new ConnectServerException(e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return message;
	}

	/* (non-Javadoc)
	 * @see async.connection.util.IServerStarterHelper#getServerPort()
	 */
	@Override
	public int getServerPort() {
		return serverSocket.getLocalPort();
	}

	/* (non-Javadoc)
	 * @see async.connection.util.IServerStarterHelper#initAndStartServer()
	 */
	@Override
	public void initAndStartServer() throws ConnectServerException {
		try {
			serverSocket = new DatagramSocket(port);
		} catch (IOException e) {
			throw new ConnectServerException(e);
		}
	}

	/* (non-Javadoc)
	 * @see async.connection.util.IServerStarterHelper#stopServer()
	 */
	@Override
	public void stopServer() {
		serverSocket.close();
	}
}
