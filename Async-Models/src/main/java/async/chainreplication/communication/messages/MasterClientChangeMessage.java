package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;

public class MasterClientChangeMessage extends MasterMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399337211401799578L;

	Client client;
	Set<Chain> chainChanges = new HashSet<Chain>();

	public MasterClientChangeMessage(Client client) {
		this.client = client;
	}


	public Set<Chain> getChainChanges() {
		return chainChanges;
	}


}
