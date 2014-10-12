package async.message.facade;

import async.chainreplication.client.server.communication.models.Outcome;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.server.models.AccountSnapshot;
import async.chainreplication.server.models.Accounts;


public class ApplicationRequestHandler implements IApplicationRequestHandler{
	Accounts accounts;
	ChainReplicationMessageHandler chainReplicationMessageHandler;

	public ApplicationRequestHandler(
			ChainReplicationMessageHandler chainReplicationMessageHandler) {
		this.accounts = new Accounts();
		this.chainReplicationMessageHandler = chainReplicationMessageHandler;
	}


	public Reply handleRequest(Request request) {
		Reply reply = checkForExistingTransactionHistoryAndReply(request);
		if(reply == null) {
			int accountNum = request.getRequestDetails().getAccountNum();
			int amount = request.getRequestDetails().getAmount();
			reply = new Reply();
			switch (request.getRequestDetails().getRequestType()) {
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


	private Reply checkForExistingTransactionHistoryAndReply(Request request) {
		if(chainReplicationMessageHandler.getHistoryOfRequests().isHistoryPresent(
				request)) {
			Reply reply = new Reply();
  			synchronized (accounts) {
	            AccountSnapshot accountSnapshot = accounts.getAccountSnapshot(
	            		request.getRequestDetails().getAccountNum());
	            reply.setBalance(accountSnapshot.getBalance());
	            reply.setOutcome(Outcome.Processed);
	            reply.setReqID(request.getRequestId());
			}
		}
		return null;
	}


	private void handleWithdrawOrTransfer(int accountNum, int amount, Reply reply) {
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


	private void handleDeposit(int accountNum, int amount, Reply reply) {
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


}
