package async.connection.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClientHelper implements IClientHelper{
	String serverHost;
	int port;

	public TCPClientHelper(String serverHost, int port) {
		this.serverHost = serverHost;
		this.port = port;
	}


	public void sendMessage(Object message) {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(serverHost, port), 3000);
			ObjectOutputStream os = null;
			try {
				os = new ObjectOutputStream(clientSocket.getOutputStream());
				os.writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(clientSocket != null) {
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
