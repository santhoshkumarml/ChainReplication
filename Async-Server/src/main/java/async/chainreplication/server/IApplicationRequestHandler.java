package async.chainreplication.server;

import java.util.Set;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Interface IApplicationRequestHandler.
 */
public interface IApplicationRequestHandler {

	/**
	 * Gets the transactional objects.
	 *
	 * @return the transactional objects
	 */
	Set<?> getTransactionalObjects();

	/**
	 * Handle ack.
	 *
	 * @param request
	 *            the request
	 */
	void handleAck(Request request);

	/**
	 * Handle request.
	 *
	 * @param request
	 *            the request
	 * @return the reply
	 */
	Reply handleRequest(Request request);

	/**
	 * Handle sync update.
	 *
	 * @param request
	 *            the request
	 * @param reply
	 *            the reply
	 */
	void handleSyncUpdate(Request request, Reply reply);

	/**
	 * Update histories.
	 *
	 * @param request
	 *            the request
	 * @param reply
	 *            the reply
	 */
	void updateHistories(Request request, Reply reply);

	/**
	 * Update transactional object.
	 *
	 * @param transactionalObject
	 *            the transactional object
	 */
	void updateTransactionalObject(Object transactionalObject);
}
