package async.chainreplication.app.server.handler;

import async.app.models.AccountSnapshot;
import async.app.models.Accounts;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.application.models.ApplicationRequest;
import async.chainreplication.application.models.Outcome;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.server.ServerMessageHandler;
import async.chainreplication.server.IApplicationRequestHandler;


public class ApplicationRequestHandler implements IApplicationRequestHandler{
	Accounts accounts;
	ServerMessageHandler chainReplicationMessageHandler;

	public ApplicationRequestHandler(
			ServerMessageHandler chainReplicationMessageHandler) {
		this.accounts = new Accounts();
		this.chainReplicationMessageHandler = chainReplicationMessageHandler;
	}

	private ApplicationReply checkForExistingTransactionHistoryAndReply(Request request) {
		if(chainReplicationMessageHandler.getHistoryOfRequests().isHistoryPresent(
				request)) {
			ApplicationReply reply = new ApplicationReply();
			ApplicationRequest applicationRequest = (ApplicationRequest)request;
			synchronized (accounts) {
				AccountSnapshot accountSnapshot = accounts.getAccountSnapshot(
						applicationRequest.getAccountNum());
				reply.setBalance(accountSnapshot.getBalance());
				//TODO : Inconsistent history
				reply.setOutcome(Outcome.Processed);
				reply.setReqID(request.getRequestId());
			}
		}
		return null;
	}


	private void handleWithdrawOrTransfer(int accountNum, int amount, ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = 
					this.accounts.getAccountSnapshot(accountNum);
			float balance;
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
			reply.setOutcome(Outcome.Processed);
			reply.setBalance(balance);
		}
	}
	
	private void handleGetBalance(int accountNum, ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = 
					this.accounts.getAccountSnapshot(accountNum);
			reply.setOutcome(Outcome.Processed);
			reply.setBalance(accountSnapshot.getBalance());
			reply.setAccountNum(accountSnapshot.getAccountNum());
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
			//this.chainReplicationMessageHandler.getHistoryOfRequests().addToHistory(request);
			//this.chainReplicationMessageHandler.getSentHistory().addToSentHistory(request.getRequestId());
		}
		return reply;
	}



	@Override
	public void handleSyncUpdate(Reply reply) {
		synchronized (accounts) {
			ApplicationReply applicationReply = (ApplicationReply)reply;
			AccountSnapshot accountSnapshot =  
					accounts.getAccountSnapshot(applicationReply.getAccountNum());
			accountSnapshot.setBalance(applicationReply.getBalance());
		}
	}


}
