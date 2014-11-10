package async.connection.util;

// TODO: Auto-generated Javadoc
/**
 * The Interface IClientHelper.
 */
public interface IClientHelper {
	
	/**
	 * Send message.
	 *
	 * @param message the message
	 * @throws ConnectClientException the connect client exception
	 */
	public void sendMessage(Object message) throws ConnectClientException;

}
