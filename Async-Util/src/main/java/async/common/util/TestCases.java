package async.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import async.chainreplication.master.models.Client;

// TODO: Auto-generated Javadoc
/**
 * The Class TestCases.
 */
public class TestCases implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -397223799879846009L;

	/** The client. */
	Client client;

	/** The requests. */
	List<RequestWithChain> requests = new ArrayList<RequestWithChain>();

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Gets the requests.
	 *
	 * @return the requests
	 */
	public List<RequestWithChain> getRequests() {
		return requests;
	}

	/**
	 * Sets the client.
	 *
	 * @param client
	 *            the new client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestCases [client=" + client + "," + requests.toString() + "]";
	}

}