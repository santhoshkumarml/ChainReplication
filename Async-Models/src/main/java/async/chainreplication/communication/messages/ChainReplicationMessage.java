package async.chainreplication.communication.messages;

import java.io.Serializable;

public abstract class ChainReplicationMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8967268676106433943L;
	
	Priority priority;

	public ChainReplicationMessage(Priority priority) {
		this.priority = priority;
	}

	public Priority getPriority() {
		return priority;
	}
}
