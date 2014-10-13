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

public class UDPServer {

	public static void main(String[] args) {
		IServerStarterHelper udpServer = new UDPServerStarterHelper(10111);
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
		IClientHelper clientHelper = new UDPClientHelper("localhost", 10121);
		try {
			clientHelper.sendMessage(
					new RequestMessage(
							new Request(new Client("3", "localhost", 3000), "3000")));
		} catch (ConnectClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
