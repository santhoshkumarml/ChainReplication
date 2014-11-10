package async.common.util;

import java.io.Serializable;

import async.chainreplication.client.server.communication.models.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestWithChain.
 */
public class RequestWithChain implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8331602387525625831L;

	/** The request. */
	Request request;
	
	/** The chain name. */
	String chainName;

	/**
	 * Instantiates a new request with chain.
	 *
	 * @param request the request
	 * @param chainName the chain name
	 */
	public RequestWithChain(Request request, String chainName) {
		super();
		this.request = request;
		this.chainName = chainName;
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
	 * Gets the request.
	 *
	 * @return the request
	 */
	public Request getRequest() {
		return request;
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
	 * Sets the request.
	 *
	 * @param request the new request
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + request.toString() + ", chainName=" + chainName + "]";
	}

}
