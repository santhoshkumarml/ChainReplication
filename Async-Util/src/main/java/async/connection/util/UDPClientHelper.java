package async.connection.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClientHelper implements IClientHelper{
	String serverHost;
	int port;

	public UDPClientHelper(String serverHost, int port) {
		this.serverHost = serverHost;
		this.port = port;
	}

	public void sendMessage(Object message) {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
			byte[] data = null;
			ObjectOutputStream os = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
				os = new ObjectOutputStream(baos);
				os.writeObject(message);
				data = baos.toByteArray();
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
			try
			{
				DatagramPacket dp = new DatagramPacket(data , data.length ,
						InetAddress.getByName(serverHost) , port);
				clientSocket.send(dp);     
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(clientSocket != null) {
				clientSocket.close();
			}
		}
	}
}