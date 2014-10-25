package async.master;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChainChanges {
	Set<String> chainsChanged = new HashSet<String>();
	Map<String, Set<String>> chainToServersChanged = new HashMap<String, Set<String>>();
	public Set<String> getChainsChanged() {
		return chainsChanged;
	}
	public Map<String, Set<String>> getChainToServersChanged() {
		return chainToServersChanged;
	}

}
