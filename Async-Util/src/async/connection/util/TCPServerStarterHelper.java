package async.connection.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerStarterHelper {
	ServerSocket serverSocket;
	public void initAndStartTCPServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public Object acceptAndReadObjectFromTCPConnections() {
		Object message = null;
		Socket serviceSocket = null;
		try {
			serviceSocket = serverSocket.accept();
			ObjectInputStream ois = new ObjectInputStream(serviceSocket.getInputStream());
			message =  ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
	public void stopTcpServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
