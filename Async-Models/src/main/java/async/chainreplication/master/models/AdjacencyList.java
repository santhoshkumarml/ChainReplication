package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class AdjacencyList.
 */
public class AdjacencyList implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1825439574320948280L;

	/** The predecessor. */
	Server predecessor;

	/** The sucessor. */
	Server sucessor;

	/**
	 * Instantiates a new adjacency list.
	 *
	 * @param predecessor
	 *            the predecessor
	 * @param sucessor
	 *            the sucessor
	 */
	public AdjacencyList(Server predecessor, Server sucessor) {
		this.predecessor = predecessor;
		this.sucessor = sucessor;
	}

	/**
	 * Gets the predecessor.
	 *
	 * @return the predecessor
	 */
	public Server getPredecessor() {
		return predecessor;
	}

	/**
	 * Gets the sucessor.
	 *
	 * @return the sucessor
	 */
	public Server getSucessor() {
		return sucessor;
	}

	/**
	 * Sets the predecessor.
	 *
	 * @param predecessor
	 *            the new predecessor
	 */
	public void setPredecessor(Server predecessor) {
		this.predecessor = predecessor;
	}

	/**
	 * Sets the sucessor.
	 *
	 * @param sucessor
	 *            the new sucessor
	 */
	public void setSucessor(Server sucessor) {
		this.sucessor = sucessor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String predecessorString = "";
		String sucessorString = "";
		if (predecessor != null) {
			predecessorString = predecessor.getServerId();
		}
		if (sucessor != null) {
			sucessorString = sucessor.getServerId();
		}
		return "AdjacencyList [predecessor=" + predecessorString
				+ ", sucessor=" + sucessorString + "]";
	}

}
