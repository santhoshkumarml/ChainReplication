package async.chainreplication.client;

import async.chainreplication.client.exception.ClientChainReplicationException;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.threads.MasterUpdateListenerThread;
import async.chainreplication.client.threads.ResponseListener;
import async.chainreplication.communication.messages.RequestMessage;
import async.chainreplication.master.models.Client;
import async.chainreplicaton.client.message.ClientRequestMessage;
import async.common.util.Config;
import async.common.util.ConfigUtil;
import async.common.util.RequestWithChain;
import async.common.util.TestCases;
import aync.chainreplication.base.impl.ChainReplicationImpl;


public class ClientImpl extends ChainReplicationImpl{
	long responseWaitTime = 10000;
	MasterUpdateListenerThread masterUpdateListener;
	ResponseListener responseListener;
	ClientChainReplicationFacade clientChainReplicationFacade;
	String clientId;

	public static void main(String args[]) {
		Config config = ConfigUtil.deserializeFromFile(args[0]);
		ClientImpl clientImpl = new ClientImpl(
				config,
				args[1]);
		clientImpl.init();
		try {
			clientImpl.performOperations(config);
		} catch (ClientChainReplicationException e) {
			e.printStackTrace();
		}
		clientImpl.stop();
	}

	public ClientImpl(Config config, String clientId) {
		super(clientId);
		this.clientId = clientId;
		try {
			clientChainReplicationFacade = new ClientChainReplicationFacade(
					config.getClients().get(clientId), config.getChains(),
					config.getMaster(), this);
			this.responseWaitTime = config.getClients().get(clientId).getResponseWaitTime();
		} catch (ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
		}
		this.logMessage("Client Started:"+config.getClients().get(clientId));
	}

	public ClientChainReplicationFacade getClientChainReplicationFacade() {
		return clientChainReplicationFacade;
	}

	public Client getClient() {
		return this.clientChainReplicationFacade.getClientMessageHandler().getClient();
	}


	private void performOperations(Config config) throws ClientChainReplicationException {
		TestCases testcases = config.getTestCases().get(config.getClients().get(clientId));
		for(RequestWithChain request : testcases.getRequests()) {
			int retryCount = request.getRequest().getRetryCount();
			int retry = 0;
			while(retry <=retryCount) {
				RequestMessage requestMessage = new RequestMessage(request.getRequest());
				ClientRequestMessage clientRequestMessage = new ClientRequestMessage(request.getChainName(), requestMessage);
				this.clientChainReplicationFacade.deliverMessage(clientRequestMessage);
				try {
					Thread.sleep(responseWaitTime);
				} catch(InterruptedException e) {
					throw new ClientChainReplicationException(e);
				}
				Reply reply = this.clientChainReplicationFacade.readResponsesForRequest(request.getRequest());
				retry++;
				if(reply != null)
					this.logMessage("Reply from Server for request - "+request.toString()+":"+reply.toString());
				else { 
					this.logMessage("Did not get a reply for request - "+ request.toString());
					if(retry<=retryCount) {
						this.logMessage("Retrying request again (retry count left:"+(retryCount-retry)+") - "+ request.toString());	
					}
				}
				
			}
		}
	}

	public void init() {
		super.init();
		//masterUpdateListener = new MasterUpdateListenerThread(this);
		//masterUpdateListener.start();
		try {
			responseListener = new ResponseListener(this);
		} catch (ClientChainReplicationException e) {
			this.logMessage(e.getMessage());
			e.printStackTrace();
			this.stop();
		}
		responseListener.start();
		this.logMessage("Client Started");
	}

	public void stop() {
		this.logMessage("Client Stopping");
		responseListener.stopThread();
		super.stop();
		System.exit(-1);
	}

	public void logMessage(String message) {
		this.getLogMessages().enqueueMessageObject(message);
	}
}
