package asyn.chainreplicationa.app.main;

import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.master.models.Client;
import async.connection.util.ConnectClientException;
import async.connection.util.ConnectServerException;
import async.connection.util.IClientHelper;
import async.connection.util.IServerStarterHelper;
import async.connection.util.UDPClientHelper;
import async.connection.util.UDPServerStarterHelper;

public class UDPClient {

	public UDPClient() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		IClientHelper clientHelper = new UDPClientHelper("localhost", 10111);
		try {
			clientHelper.sendMessage(
					new RequestMessage(
							new Request(new Client("1", "localhost", 2000), "2000")));
		} catch (ConnectClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IServerStarterHelper udpServer = new UDPServerStarterHelper(10121);
		Object object = null;
		try {
			udpServer.initAndStartServer();
			object = udpServer.acceptAndReadObjectConnection();
			System.out.println(object);
			udpServer.stopServer();
		} catch (ConnectServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
