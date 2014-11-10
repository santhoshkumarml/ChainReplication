package async.chainreplication.communication.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class BulkSyncMessage.
 */
public class BulkSyncMessage extends ChainReplicationMessage implements
		Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -499882516978443630L;

	/** The sync messages. */
	List<ResponseOrSyncMessage> syncMessages = new ArrayList<ResponseOrSyncMessage>();

	/**
	 * Instantiates a new bulk sync message.
	 */
	public BulkSyncMessage() {
		super(Priority.HIGHER_PRIORITY);
	}

	/**
	 * Gets the sync messages.
	 *
	 * @return the sync messages
	 */
	public List<ResponseOrSyncMessage> getSyncMessages() {
		return syncMessages;
	}

	@Override
	public String toString() {
		return "BulkSyncMessage [syncMessages=" + syncMessages + "]";
	}
	
	

}
