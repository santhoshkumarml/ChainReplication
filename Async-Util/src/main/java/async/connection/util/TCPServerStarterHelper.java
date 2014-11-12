package async.connection.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

// TODO: Auto-generated Javadoc
/**
 * The Class TCPServerStarterHelper.
 */
public class TCPServerStarterHelper implements IServerStarterHelper {

	/** The server socket. */
	ServerSocket serverSocket;

	/** The port. */
	int port;

	/**
	 * Instantiates a new TCP server starter helper.
	 *
	 * @param port
	 *            the port
	 */
	public TCPServerStarterHelper(int port) {
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * async.connection.util.IServerStarterHelper#acceptAndReadObjectConnection
	 * ()
	 */
	@Override
	public Object acceptAndReadObjectConnection() throws ConnectServerException {
		Object message = null;
		Socket serviceSocket = null;
		try {
			serviceSocket = serverSocket.accept();
			final ObjectInputStream ois = new ObjectInputStream(
					serviceSocket.getInputStream());
			message = ois.readObject();
		} catch (final IOException e) {
			throw new ConnectServerException(e);
		} catch (final ClassNotFoundException e) {
			throw new ConnectServerException(e);
		} finally {
			if (serviceSocket != null) {
				try {
					serviceSocket.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see async.connection.util.IServerStarterHelper#getServerPort()
	 */
	@Override
	public int getServerPort() {
		return serverSocket.getLocalPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see async.connection.util.IServerStarterHelper#initAndStartServer()
	 */
	@Override
	public void initAndStartServer() throws ConnectServerException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (final IOException e) {
			throw new ConnectServerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see async.connection.util.IServerStarterHelper#stopServer()
	 */
	@Override
	public void stopServer() {
		try {
			serverSocket.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
