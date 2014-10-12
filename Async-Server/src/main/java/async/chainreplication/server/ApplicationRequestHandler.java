package async.chainreplication.server;

import async.chainreplication.application.models.AccountSnapshot;
import async.chainreplication.application.models.Accounts;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.application.models.ApplicationRequest;
import async.chainreplication.application.models.Outcome;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;


public class ApplicationRequestHandler implements IApplicationRequestHandler{
	Accounts accounts;
	ChainReplicationMessageHandler chainReplicationMessageHandler;

	public ApplicationRequestHandler(
			ChainReplicationMessageHandler chainReplicationMessageHandler) {
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
				balance -= amount;
				accountSnapshot.setBalance(balance);
				reply.setOutcome(Outcome.Processed);
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
			accountSnapshot = accounts.addAccount(accountNum);
			float balance = accountSnapshot.getBalance();
			balance += amount;
			reply.setOutcome(Outcome.Processed);
			reply.setBalance(balance);
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
			switch (applicationRequest.getRequestType()) {
			case DEPOSIT:
				handleDeposit(accountNum, amount, reply);
				break;
			case WITHDRAW:
				handleWithdrawOrTransfer(accountNum, amount, reply);
				break;
			case TRANSFER:
				handleWithdrawOrTransfer(accountNum, amount, reply);
				break;
			default:
				break;
			}
			reply.setReqID(request.getRequestId());
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
