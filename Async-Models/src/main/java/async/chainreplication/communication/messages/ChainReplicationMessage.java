package async.chainreplication.communication.messages;

import java.io.Serializable;

public abstract class ChainReplicationMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8967268676106433943L;
	
	Priority pritority;

	public ChainReplicationMessage(Priority pritority) {
		this.pritority = pritority;
	}

	public Priority getPritority() {
		return pritority;
	}
}
