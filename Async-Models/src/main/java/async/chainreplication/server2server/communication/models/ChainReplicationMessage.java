package async.chainreplication.server2server.communication.models;

import java.io.Serializable;

public class ChainReplicationMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8967268676106433943L;

	public enum ChainReplicationMessageEnum {
		SYNC,
		ACK,
		INTRANSIT_UPDATES;
	}

}
