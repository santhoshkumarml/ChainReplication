package async.connection.util;

// TODO: Auto-generated Javadoc
/**
 * The Interface IServerStarterHelper.
 */
public interface IServerStarterHelper {

	/**
	 * Accept and read object connection.
	 *
	 * @return the object
	 * @throws ConnectServerException
	 *             the connect server exception
	 */
	Object acceptAndReadObjectConnection() throws ConnectServerException;

	/**
	 * Gets the server port.
	 *
	 * @return the server port
	 */
	int getServerPort();

	/**
	 * Inits the and start server.
	 *
	 * @throws ConnectServerException
	 *             the connect server exception
	 */
	void initAndStartServer() throws ConnectServerException;

	/**
	 * Stop server.
	 */
	void stopServer();

}
