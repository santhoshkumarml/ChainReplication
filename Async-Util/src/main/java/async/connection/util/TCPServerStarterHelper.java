package async.connection.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerStarterHelper implements IServerStarterHelper{
	ServerSocket serverSocket;
	int port;

	public TCPServerStarterHelper(int port) {
		this.port = port;
	}

	@Override
	public void initAndStartServer() throws ConnectServerException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new ConnectServerException(e);
		}
	}

	@Override
	public int getServerPort() {
		return serverSocket.getLocalPort();
	}


	@Override
	public Object acceptAndReadObjectConnection() throws ConnectServerException {
		Object message = null;
		Socket serviceSocket = null;
		try {
			serviceSocket = serverSocket.accept();
			ObjectInputStream ois = new ObjectInputStream(serviceSocket.getInputStream());
			message =  ois.readObject();
		} catch (IOException e) {
			throw new ConnectServerException(e);
		} catch (ClassNotFoundException e) {
			throw new ConnectServerException(e);
		} finally {
			if(serviceSocket != null) {
				try {
					serviceSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return message;
	}

	@Override	
	public void stopServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
