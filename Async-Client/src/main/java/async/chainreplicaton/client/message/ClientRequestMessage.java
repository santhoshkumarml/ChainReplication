package async.chainreplicaton.client.message;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.Priority;
import async.chainreplication.communication.messages.RequestMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientRequestMessage.
 */
public class ClientRequestMessage extends ChainReplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5500855820644003266L;

	/** The chain name. */
	String chainName;

	/** The request message. */
	RequestMessage requestMessage;

	/**
	 * Instantiates a new client request message.
	 *
	 * @param chainName
	 *            the chain name
	 * @param requestMessage
	 *            the request message
	 */
	public ClientRequestMessage(String chainName, RequestMessage requestMessage) {
		super(Priority.HIGH_PRIORITY);
		this.chainName = chainName;
		this.requestMessage = requestMessage;
	}

	/**
	 * Gets the chain name.
	 *
	 * @return the chain name
	 */
	public String getChainName() {
		return chainName;
	}

	/**
	 * Gets the request message.
	 *
	 * @return the request message
	 */
	public RequestMessage getRequestMessage() {
		return requestMessage;
	}

	/**
	 * Sets the chain name.
	 *
	 * @param chainName
	 *            the new chain name
	 */
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	/**
	 * Sets the request message.
	 *
	 * @param requestMessage
	 *            the new request message
	 */
	public void setRequestMessage(RequestMessage requestMessage) {
		this.requestMessage = requestMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClientRequestMessage [chainName=" + chainName + ","
				+ requestMessage.toString() + "]";
	}

}
