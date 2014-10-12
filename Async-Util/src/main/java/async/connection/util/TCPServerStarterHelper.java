package async.connection.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerStarterHelper implements IServerStarterHelper{
	ServerSocket serverSocket;
	
	public TCPServerStarterHelper() {
		initAndStartTCPServer();
	}
	
	private void initAndStartTCPServer() {
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	
	@Override
	public int getServerPort() {
		return serverSocket.getLocalPort();
	}


	@Override
	public Object acceptAndReadObjectConnection() {
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
