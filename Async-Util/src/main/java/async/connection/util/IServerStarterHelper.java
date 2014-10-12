package async.connection.util;


public interface IServerStarterHelper {

	int getServerPort();
	
	void initAndStartServer() throws ConnectServerException;

	Object acceptAndReadObjectConnection() throws ConnectServerException;

	void stopServer();

}
