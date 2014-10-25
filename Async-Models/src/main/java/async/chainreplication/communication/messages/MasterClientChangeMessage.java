package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.master.models.Chain;

public class MasterClientChangeMessage extends MasterMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4399337211401799578L;

	Set<Chain> chainChanges = new HashSet<Chain>();

	public MasterClientChangeMessage(Set<Chain> chainChanges) {
		this.chainChanges = chainChanges;
	}


	public Set<Chain> getOtherChains() {
		return chainChanges;
	}


}
