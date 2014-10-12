package async.chainreplication.communication.message.models;

import async.chainreplication.master.models.Chain;

public class MasterOtherChainChangeMessage extends MasterMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1891843269349186453L;

	Chain chain;

	public MasterOtherChainChangeMessage(Chain chain) {
		this.chain = chain;
	}

	public Chain getChain() {
		return chain;
	}
}
