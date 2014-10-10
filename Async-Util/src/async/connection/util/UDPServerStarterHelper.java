package async.connection.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerStarterHelper {
	DatagramSocket serverSocket;

	public void initAndStartUDPServer(int port) {
		try {
			serverSocket = new DatagramSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public Object acceptAndReadObjectFromUDPConnections() {
		Object message = null;
		byte[] receiveData = new byte[2048];
	    ObjectInputStream oos = null;
		try {
			DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length );
			serverSocket.receive(packet);
			ByteArrayInputStream baos = new ByteArrayInputStream(receiveData);
			oos = new ObjectInputStream(baos);
			message = oos.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(oos != null) {
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

	public void stopUDPServer() {
		serverSocket.close();
	}


}
