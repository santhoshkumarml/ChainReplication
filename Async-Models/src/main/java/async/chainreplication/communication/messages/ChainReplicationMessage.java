package async.chainreplication.communication.messages;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainReplicationMessage.
 */
public abstract class ChainReplicationMessage implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8967268676106433943L;

	/** The priority. */
	Priority priority;

	/**
	 * Instantiates a new chain replication message.
	 *
	 * @param priority
	 *            the priority
	 */
	public ChainReplicationMessage(Priority priority) {
		this.priority = priority;
	}

	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}
}
