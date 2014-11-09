package async.chainreplication.communication.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BulkSyncMessage extends ChainReplicationMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -499882516978443630L;
	
	List<ResponseOrSyncMessage> syncMessages = new ArrayList<ResponseOrSyncMessage>();
	
	public BulkSyncMessage() {
		super(Priority.HIGHER_PRIORITY);
	}

	public List<ResponseOrSyncMessage> getSyncMessages() {
		return syncMessages;
	}

}
