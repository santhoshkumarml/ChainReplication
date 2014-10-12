package async.message.facade;

import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.master.models.Master;
import async.chainreplication.master.models.Server;
import async.chainreplication.server.models.Accounts;
import async.chainreplication.server.models.HistoryOfRequests;
import async.chainreplication.server.models.SentHistory;
import async.chainreplication.server2server.communication.models.AckMessage;
import async.chainreplication.server2server.communication.models.ChainReplicationMessage;
import async.chainreplication.server2server.communication.models.SyncMessage;
import async.connection.util.TCPClientHelper;
import async.connection.util.UDPClientHelper;

public class ChainReplicationMessageHandler {
	Request currentRequest;
	Reply currentReply;
	SentHistory sentHistory;
	HistoryOfRequests historyOfRequests;
	Accounts accounts;

	Server server;
	Master master;

	TCPClientHelper tcpClientHelper;
	UDPClientHelper udpClientHelper;

	public ChainReplicationMessageHandler(Server server, Master master) {
		this.server  = server;
		this.master =  master;
	}

	public Request getCurrentRequest() {
		return currentRequest;
	}

	public void setCurrentRequest(Request currentRequest) {
		this.currentRequest = currentRequest;
	}

	public Reply getCurrentReply() {
		return currentReply;
	}

	public void setCurrentReply(Reply currentReply) {
		this.currentReply = currentReply;
	}

	public SentHistory getSentHistory() {
		return sentHistory;
	}

	public void setSentHistory(SentHistory sentHistory) {
		this.sentHistory = sentHistory;
	}

	public HistoryOfRequests getHistoryOfRequests() {
		return historyOfRequests;
	}

	public void setHistoryOfRequests(HistoryOfRequests historyOfRequests) {
		this.historyOfRequests = historyOfRequests;
	}

	public void sync(Request request, Reply reply) {
		this.setCurrentRequest(request);
		this.setCurrentReply(reply);
		accounts.updateAccount(request.getRequestDetails().getAccountNum(), reply.getBalance());
		historyOfRequests.addToHistory(request);
		Server sucessor = this.server.getAdjacencyList().getSucessor();
		if(sucessor != null) {
			tcpClientHelper = new TCPClientHelper(
					sucessor.getServerProcessDetails().getHost(),
					sucessor.getServerProcessDetails().getTcpPort());
			ChainReplicationMessage syncMessage = new SyncMessage(request, reply);
			tcpClientHelper.sendMessageOverTCPConnection(syncMessage);

			//send sync
		} else {
			udpClientHelper = new UDPClientHelper(
					request.getClient().getClientProcessDetails().getHost()	,
					request.getClient().getClientProcessDetails().getUdpPort());
			udpClientHelper.sendMessageOverUDPConnection(reply);
		}
		sentHistory.addToSentHistory(request.getRequestId());
	}

	public void ACK(Request request) {
		sentHistory.removeFromSent(request.getRequestId());
		Server predecessor = this.server.getAdjacencyList().getPredecessor();
		if(predecessor != null) {
			tcpClientHelper = new TCPClientHelper(
					predecessor.getServerProcessDetails().getHost(),
					predecessor.getServerProcessDetails().getTcpPort());
			ChainReplicationMessage ackMessage = new AckMessage(request);
			//change it to ACK Message
			tcpClientHelper.sendMessageOverTCPConnection(ackMessage);
		}

	}

	/*public void IN_TRANSIT_UPDATES(String lastRequestId) {
		synchronized(sentHistory) {
			for(String requestId : sentHistory.getRequestIds()) {
				//get latest Value for the account and return it
			}
		}
	}*/
}
