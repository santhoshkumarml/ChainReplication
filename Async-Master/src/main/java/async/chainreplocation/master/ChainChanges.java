package async.chainreplocation.master;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ChainChanges.
 */
public class ChainChanges {
	
	/** The chains changed. */
	Set<String> chainsChanged = new HashSet<String>();
	
	/** The chain to servers changed. */
	Map<String, Set<String>> chainToServersChanged = new HashMap<String, Set<String>>();

	/**
	 * Gets the chains changed.
	 *
	 * @return the chains changed
	 */
	public Set<String> getChainsChanged() {
		return chainsChanged;
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
