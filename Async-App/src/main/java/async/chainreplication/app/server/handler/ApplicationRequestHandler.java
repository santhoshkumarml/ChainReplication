package async.chainreplication.app.server.handler;

import async.app.models.AccountSnapshot;
import async.app.models.Accounts;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.application.models.ApplicationRequest;
import async.chainreplication.application.models.ApplicationRequestKey;
import async.chainreplication.application.models.Outcome;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.server.communication.models.RequestKey;
import async.chainreplication.server.IApplicationRequestHandler;
import async.chainreplication.server.ServerMessageHandler;


public class ApplicationRequestHandler implements IApplicationRequestHandler{
	Accounts accounts;
	ServerMessageHandler chainReplicationMessageHandler;

	public ApplicationRequestHandler(
			ServerMessageHandler chainReplicationMessageHandler) {
		this.accounts = new Accounts();
		this.chainReplicationMessageHandler = chainReplicationMessageHandler;
	}

	private ApplicationReply checkForExistingTransactionHistoryAndReply(Request request) {
		ApplicationRequest applicationRequest = (ApplicationRequest)request;
		ApplicationRequestKey requestKey = new ApplicationRequestKey(applicationRequest.getRequestId(),
				applicationRequest.getAccountNum());
		if(chainReplicationMessageHandler.getHistoryOfRequests().isHistoryPresent(
				requestKey)) {
			ApplicationReply reply = new ApplicationReply();
			if(!isInconsistentWithHistory(request)) {
				reply = 
						(ApplicationReply)chainReplicationMessageHandler.getHistoryOfRequests().getExisistingReply(
								requestKey);
			} else {
				synchronized (accounts) {
					AccountSnapshot accountSnapshot = accounts.getAccountSnapshot(
							applicationRequest.getAccountNum());
					reply.setBalance(accountSnapshot.getBalance());
					reply.setOutcome(Outcome.InconsistentWithHistory);
					reply.setReqID(request.getRequestId());
				}
			}
			return reply;
		}
		return null;
	}


	private boolean isInconsistentWithHistory(Request request) {
		ApplicationRequest applicationRequest = (ApplicationRequest)request;
		ApplicationRequest existingRequest = 
				(ApplicationRequest)this.chainReplicationMessageHandler.getHistoryOfRequests().getExisistingRequest(
						new ApplicationRequestKey(applicationRequest.getRequestId(),
								applicationRequest.getAccountNum()));
		if(applicationRequest.equals(existingRequest)) {
			return false;
		}
		return true;
	}

	private void handleWithdrawOrTransfer(int accountNum, int amount, ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = 
					this.accounts.getAccountSnapshot(accountNum);
			float balance;
			reply.setAccountNum(accountNum);
			if(accountSnapshot != null)  {
				balance = accountSnapshot.getBalance();
				if(balance>=amount) {
					balance -= amount;
					accountSnapshot.setBalance(balance);
					reply.setOutcome(Outcome.Processed);
				} else {
					reply.setOutcome(Outcome.InsufficientFunds);
				}
			} else {
				accountSnapshot = accounts.addAccount(accountNum);
				balance = accountSnapshot.getBalance();
				reply.setOutcome(Outcome.InsufficientFunds);
			}
			reply.setBalance(balance);
		}
	}


	private void handleDeposit(int accountNum, int amount, ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = 
					this.accounts.getAccountSnapshot(accountNum);
			if(accountSnapshot == null) {
				accountSnapshot = accounts.addAccount(accountNum);
			}
			float balance = accountSnapshot.getBalance();
			balance += amount;
			reply.setAccountNum(accountNum);
			reply.setOutcome(Outcome.Processed);
			reply.setBalance(balance);
		}
	}

	private void handleGetBalance(int accountNum, ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = 
					this.accounts.getAccountSnapshot(accountNum);
			reply.setOutcome(Outcome.Processed);
			reply.setAccountNum(accountNum);			
			if(accountSnapshot != null) {
				reply.setBalance(accountSnapshot.getBalance());
			} else {
				reply.setBalance(0);

			}
		}

	}

	//--------------------------------------------------------------------------
	//Handler Methods
	@Override
	public Reply handleRequest(Request request) {
		ApplicationReply reply = checkForExistingTransactionHistoryAndReply(request);
		ApplicationRequest applicationRequest = (ApplicationRequest)request;
		if(reply == null) {
			int accountNum = applicationRequest.getAccountNum();
			int amount = applicationRequest.getAmount();
			reply = new ApplicationReply();
			switch (applicationRequest.getApplicationRequestType()) {
			case DEPOSIT:
				handleDeposit(accountNum, amount, reply);
				break;
			case WITHDRAW:
				handleWithdrawOrTransfer(accountNum, amount, reply);
				break;
			case TRANSFER:
				handleWithdrawOrTransfer(accountNum, amount, reply);
				break;
			case GET_BALANCE:
				handleGetBalance(accountNum, reply);
				break;
			default:
				break;
			}
			reply.setReqID(request.getRequestId());
		}
		return reply;
	}

	@Override
	public void updateHistories(Request request, Reply reply) {
		RequestKey requestKey = new ApplicationRequestKey(
				request.getRequestId(), ((ApplicationRequest)request).getAccountNum());
		this.chainReplicationMessageHandler.getHistoryOfRequests().addToHistory(requestKey, request, reply);
		this.chainReplicationMessageHandler.getSentHistory().addToSentHistory(requestKey);
	}

	@Override 
	public void handleAck(Request request) {
		RequestKey requestKey = new ApplicationRequestKey(
				request.getRequestId(), ((ApplicationRequest)request).getAccountNum());
		this.chainReplicationMessageHandler.getSentHistory().removeFromSent(requestKey);	
	}



	@Override
	public void handleSyncUpdate(Request request, Reply reply) {
		synchronized (accounts) {
			ApplicationReply applicationReply = (ApplicationReply)reply;
			this.updateHistories(request, reply);
			AccountSnapshot accountSnapshot =  
					accounts.getAccountSnapshot(applicationReply.getAccountNum());
			if(accountSnapshot != null) //For get balance if we give a sync it will not find any account
				accountSnapshot.setBalance(applicationReply.getBalance());
		}
	}


}
