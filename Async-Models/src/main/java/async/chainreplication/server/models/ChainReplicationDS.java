package async.chainreplication.server.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import async.chainreplication.master.models.Bank;
import async.chainreplication.master.models.Server;

public class ChainReplicationDS {
	List<ChainRequest> sent = new LinkedList<ChainRequest>();
	List<ChainRequest> transfer_ACK = new LinkedList<ChainRequest>();
	Server server;
	Map<String, Bank> otherBankDs = new HashMap<String, Bank>();

}
