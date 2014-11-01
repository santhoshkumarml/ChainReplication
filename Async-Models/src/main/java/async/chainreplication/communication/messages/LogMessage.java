/**
 * 
 */
package async.chainreplication.communication.messages;

/**
 * @author Santhosh Kumar Manavasi Lakshminarayanan
 *
 */
public class LogMessage extends ChainReplicationMessage {

	private static final long serialVersionUID = -8797873379311671762L;

	String message;
	
	public LogMessage(String message) {
		super(Priority.NORMAL_PRIORITY);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "LogMessage [message=" + message + "]";
	}

}
