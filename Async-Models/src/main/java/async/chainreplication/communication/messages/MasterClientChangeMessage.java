package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.Set;

import async.chainreplication.master.models.Chain;
import async.chainreplication.master.models.Client;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterClientChangeMessage.
 */
public class MasterClientChangeMessage extends MasterMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4399337211401799578L;

	/** The client. */
	Client client;
	
	/** The chain changes. */
	Set<Chain> chainChanges = new HashSet<Chain>();

	/**
	 * Instantiates a new master client change message.
	 *
	 * @param client the client
	 */
	public MasterClientChangeMessage(Client client) {
		super(Priority.REALTIME_PRIORITY);
		this.client = client;
	}

	/**
	 * Gets the chain changes.
	 *
	 * @return the chain changes
	 */
	public Set<Chain> getChainChanges() {
		return chainChanges;
	}

	@Override
	public String toString() {
		return "MasterClientChangeMessage [client=" + client
				+ ", chainChanges=" + chainChanges + "]";
	}

}
