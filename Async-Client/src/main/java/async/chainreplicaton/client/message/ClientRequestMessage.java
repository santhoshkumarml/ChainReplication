package async.chainreplicaton.client.message;

import async.chainreplication.communication.messages.ChainReplicationMessage;
import async.chainreplication.communication.messages.RequestMessage;

public class ClientRequestMessage extends ChainReplicationMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5500855820644003266L;
	
	String chainName;
	RequestMessage requestMessage;
	
	public ClientRequestMessage(String chainName, RequestMessage requestMessage) {
		this.chainName = chainName;
		this.requestMessage = requestMessage;
	}

	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	public RequestMessage getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(RequestMessage requestMessage) {
		this.requestMessage = requestMessage;
	}

}
