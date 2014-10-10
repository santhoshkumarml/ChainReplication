package async.connection.util;

import java.io.IOException;
import java.net.Socket;

public class TCPClientHelper {
	
	Socket clientSocket;
	
    public void initSocket(String host, int port) {
    	try {
			clientSocket = new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public void sendMessageOverTCPConnection(Object message) {
    	
    }
}
