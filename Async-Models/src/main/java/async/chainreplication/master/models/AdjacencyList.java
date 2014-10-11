package async.chainreplication.master.models;

import java.io.Serializable;


public class AdjacencyList implements Serializable{
	Server predecessor;
	Server sucessor;
}
