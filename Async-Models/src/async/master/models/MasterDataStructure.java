package async.master.models;

import java.util.concurrent.ConcurrentHashMap;

public class MasterDataStructure {

	ConcurrentHashMap<Server,AdjacencyList> serverToAdjacencyList =
			new ConcurrentHashMap<Server,AdjacencyList>();

}
