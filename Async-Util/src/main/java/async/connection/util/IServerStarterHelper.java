package async.connection.util;

public interface IServerStarterHelper {

	int getServerPort();

	Object acceptAndReadObjectConnection();

	void stopServer();

}
