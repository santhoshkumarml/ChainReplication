package async.chainreplication.master.models;

import java.io.Serializable;


public class AdjacencyList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1825439574320948280L;
	Server predecessor;
	Server sucessor;
}
