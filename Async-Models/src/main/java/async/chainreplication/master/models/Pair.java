package async.chainreplication.master.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Pair.
 *
 * @param <F>
 *            the generic type
 * @param <S>
 *            the generic type
 */
public class Pair<F, S> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6579190661922473935L;

	/** The first. */
	F first;

	/** The second. */
	S second;

	/**
	 * Instantiates a new pair.
	 *
	 * @param first
	 *            the first
	 * @param second
	 *            the second
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public F getFirst() {
		return first;
	}

	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public S getSecond() {
		return second;
	}

	/**
	 * Sets the first.
	 *
	 * @param first
	 *            the new first
	 */
	public void setFirst(F first) {
		this.first = first;
	}

	/**
	 * Sets the second.
	 *
	 * @param second
	 *            the new second
	 */
	public void setSecond(S second) {
		this.second = second;
	}

}