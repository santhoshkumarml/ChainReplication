package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Chain.
 */
public class Chain implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1094492046636943340L;
	
	/** The chain name. */
	String chainName;
	
	/** The head. */
	Server head;
	
	/** The tail. */
	Server tail;

	/**
	 * Instantiates a new chain.
	 *
	 * @param bankName the bank name
	 * @param head the head
	 * @param tail the tail
	 */
	public Chain(String bankName, Server head, Server tail) {
		chainName = bankName;
		this.head = head;
		this.tail = tail;
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
	 * Gets the head.
	 *
	 * @return the head
	 */
	public Server getHead() {
		return head;
	}

	/**
	 * Gets the tail.
	 *
	 * @return the tail
	 */
	public Server getTail() {
		return tail;
	}

	/**
	 * Sets the chain name.
	 *
	 * @param chainName the new chain name
	 */
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	/**
	 * Sets the head.
	 *
	 * @param head the new head
	 */
	public void setHead(Server head) {
		this.head = head;
	}

	/**
	 * Sets the tail.
	 *
	 * @param tail the new tail
	 */
	public void setTail(Server tail) {
		this.tail = tail;
	}

}
