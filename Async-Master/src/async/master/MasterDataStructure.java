package async.master;

import java.util.concurrent.ConcurrentHashMap;

public class MasterDataStructure {

	ConcurrentHashMap<Server,AdjacencyList> serverToAdjacencyList =
			new ConcurrentHashMap<Server,AdjacencyList>();

}
