package async.chainreplocation.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainChanges.
 */
public class ChainChanges {
	
	/** The chains changed. */
	Map<String,List<Boolean>> chainsToHeadTailChanges = new HashMap<String, List<Boolean>>();
	
	/** The chain to servers changed. */
	Map<String, Set<String>> chainToServersChanged = new HashMap<String, Set<String>>();

	/**
	 * Gets the chains changed.
	 *
	 * @return the chains changed
	 */
	public Map<String,List<Boolean>> getChainsToHeadTailChanges() {
		return chainsToHeadTailChanges;
	}

	/**
	 * Gets the chain to servers changed.
	 *
	 * @return the chain to servers changed
	 */
	public Map<String, Set<String>> getChainToServersChanged() {
		return chainToServersChanged;
	}

}
