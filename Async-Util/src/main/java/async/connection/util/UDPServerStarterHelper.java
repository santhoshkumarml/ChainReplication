package async.connection.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerStarterHelper implements IServerStarterHelper{
	DatagramSocket serverSocket;
	int port;

	public UDPServerStarterHelper(int port) {
		this.port = port;
	}
	@Override
	public int getServerPort() {
		return serverSocket.getLocalPort();
	}

	@Override
	public void initAndStartServer() throws ConnectServerException {
		try {
			serverSocket = new DatagramSocket(0);
		} catch (IOException e) {
			throw new ConnectServerException(e);
		}
	}

	@Override
	public Object acceptAndReadObjectConnection() throws ConnectServerException {
		Object message = null;
		byte[] receiveData = new byte[2048];
		ObjectInputStream oos = null;
		try {
			DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(packet);
			ByteArrayInputStream baos = new ByteArrayInputStream(receiveData);
			oos = new ObjectInputStream(baos);
			message = oos.readObject();
		} catch (IOException e) {
			throw new ConnectServerException(e);
		} catch (ClassNotFoundException e) {
			throw new ConnectServerException(e);
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

	@Override
	public void stopServer() {
		serverSocket.close();
	}
}
