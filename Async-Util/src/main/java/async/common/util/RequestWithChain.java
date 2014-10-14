package async.common.util;

import java.io.Serializable;

import async.chainreplication.client.server.communication.models.Request;

public class RequestWithChain implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8331602387525625831L;
	
	Request request;
	String chainName;
	
	
	public RequestWithChain(Request request, String chainName) {
		super();
		this.request = request;
		this.chainName = chainName;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	@Override
	public String toString() {
		return "[" + request.toString() + ", chainName="
				+ chainName + "]";
	} 

}
