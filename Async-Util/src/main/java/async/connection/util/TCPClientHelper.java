package async.connection.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

// TODO: Auto-generated Javadoc
/**
 * The Class TCPClientHelper.
 */
public class TCPClientHelper implements IClientHelper {
	
	/** The server host. */
	String serverHost;
	
	/** The port. */
	int port;

	/**
	 * Instantiates a new TCP client helper.
	 *
	 * @param serverHost the server host
	 * @param port the port
	 */
	public TCPClientHelper(String serverHost, int port) {
		this.serverHost = serverHost;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see async.connection.util.IClientHelper#sendMessage(java.lang.Object)
	 */
	@Override
	public void sendMessage(Object message) throws ConnectClientException {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(serverHost, port), 3000);
			ObjectOutputStream os = null;
			try {
				os = new ObjectOutputStream(clientSocket.getOutputStream());
				os.writeObject(message);
			} catch (IOException e) {
				throw new ConnectClientException(e);
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} catch (IOException e1) {
			throw new ConnectClientException(e1);
		} finally {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
