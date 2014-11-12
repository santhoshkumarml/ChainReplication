package async.chainreplication.app.server.handler;

import java.util.Set;

import async.app.models.AccountSnapshot;
import async.app.models.Accounts;
import async.chainreplication.application.models.ApplicationReply;
import async.chainreplication.application.models.ApplicationRequest;
import async.chainreplication.application.models.ApplicationRequestKey;
import async.chainreplication.application.models.Outcome;
import async.chainreplication.client.server.communication.models.Reply;
import async.chainreplication.client.server.communication.models.Request;
import async.chainreplication.client.server.communication.models.RequestKey;
import async.chainreplication.client.server.communication.models.RequestType;
import async.chainreplication.server.IApplicationRequestHandler;
import async.chainreplication.server.ServerMessageHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationRequestHandler.
 */
public class ApplicationRequestHandler implements IApplicationRequestHandler {

	/** The accounts. */
	Accounts accounts;

	/** The chain replication message handler. */
	ServerMessageHandler chainReplicationMessageHandler;

	/**
	 * Instantiates a new application request handler.
	 *
	 * @param chainReplicationMessageHandler
	 *            the chain replication message handler
	 */
	public ApplicationRequestHandler(
			ServerMessageHandler chainReplicationMessageHandler) {
		accounts = new Accounts();
		this.chainReplicationMessageHandler = chainReplicationMessageHandler;
	}

	/**
	 * Check for existing transaction history and reply.
	 *
	 * @param request
	 *            the request
	 * @return the application reply
	 */
	private ApplicationReply checkForExistingTransactionHistoryAndReply(
			Request request) {
		final ApplicationRequest applicationRequest = (ApplicationRequest) request;
		final ApplicationRequestKey requestKey = new ApplicationRequestKey(
				applicationRequest.getRequestId(),
				applicationRequest.getAccountNum());
		if (chainReplicationMessageHandler.getHistoryOfRequests()
				.isHistoryPresent(requestKey)) {
			ApplicationReply reply = new ApplicationReply();
			if (!isInconsistentWithHistory(request)) {
				reply = (ApplicationReply) chainReplicationMessageHandler
						.getHistoryOfRequests().getExisistingReply(requestKey);
			} else {
				synchronized (accounts) {
					final AccountSnapshot accountSnapshot = accounts
							.getAccountSnapshot(applicationRequest
									.getAccountNum());
					reply.setBalance(accountSnapshot.getBalance());
					reply.setOutcome(Outcome.InconsistentWithHistory);
					reply.setReqID(request.getRequestId());
				}
			}
			return reply;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see async.chainreplication.server.IApplicationRequestHandler#
	 * getTransactionalObjects()
	 */
	@Override
	public Set<?> getTransactionalObjects() {
		return accounts.getAccountSnapShots();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * async.chainreplication.server.IApplicationRequestHandler#handleAck(async
	 * .chainreplication.client.server.communication.models.Request)
	 */
	@Override
	public void handleAck(Request request) {
		final RequestKey requestKey = new ApplicationRequestKey(
				request.getRequestId(),
				((ApplicationRequest) request).getAccountNum());
		synchronized (chainReplicationMessageHandler.getSentHistory()) {
			chainReplicationMessageHandler.getSentHistory().removeFromSent(
					requestKey);
		}
	}

	/**
	 * Handle deposit.
	 *
	 * @param accountNum
	 *            the account num
	 * @param amount
	 *            the amount
	 * @param reply
	 *            the reply
	 */
	private void handleDeposit(int accountNum, int amount,
			ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = accounts
					.getAccountSnapshot(accountNum);
			if (accountSnapshot == null) {
				accountSnapshot = accounts.addAccount(accountNum);
			}
			float balance = accountSnapshot.getBalance();
			balance += amount;
			reply.setAccountNum(accountNum);
			reply.setOutcome(Outcome.Processed);
			reply.setBalance(balance);
		}
	}

	/**
	 * Handle get balance.
	 *
	 * @param accountNum
	 *            the account num
	 * @param reply
	 *            the reply
	 */
	private void handleGetBalance(int accountNum, ApplicationReply reply) {
		synchronized (accounts) {
			final AccountSnapshot accountSnapshot = accounts
					.getAccountSnapshot(accountNum);
			reply.setOutcome(Outcome.Processed);
			reply.setAccountNum(accountNum);
			if (accountSnapshot != null) {
				reply.setBalance(accountSnapshot.getBalance());
			} else {
				reply.setBalance(0);

			}
		}

	}

	// --------------------------------------------------------------------------
	// Handler Methods
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * async.chainreplication.server.IApplicationRequestHandler#handleRequest
	 * (async.chainreplication.client.server.communication.models.Request)
	 */
	@Override
	public Reply handleRequest(Request request) {
		ApplicationReply reply = checkForExistingTransactionHistoryAndReply(request);
		final ApplicationRequest applicationRequest = (ApplicationRequest) request;
		if (reply == null) {
			final int accountNum = applicationRequest.getAccountNum();
			final int amount = applicationRequest.getAmount();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * async.chainreplication.server.IApplicationRequestHandler#handleSyncUpdate
	 * (async.chainreplication.client.server.communication.models.Request,
	 * async.chainreplication.client.server.communication.models.Reply)
	 */
	@Override
	public void handleSyncUpdate(Request request, Reply reply) {
		if (request.getRequestType() != RequestType.QUERY) {
			synchronized (accounts) {
				final ApplicationReply applicationReply = (ApplicationReply) reply;
				this.updateHistories(request, reply);
				AccountSnapshot accountSnapshot = accounts
						.getAccountSnapshot(applicationReply.getAccountNum());
				if (accountSnapshot != null) {
					accountSnapshot.setBalance(applicationReply.getBalance());
				} else {
					// if (request.getRequestType() != RequestType.QUERY) {
					accountSnapshot = accounts.addAccount(applicationReply
							.getAccountNum());
					accountSnapshot.setBalance(applicationReply.getBalance());
					// }
				}
			}
		}
	}

	/**
	 * Handle withdraw or transfer.
	 *
	 * @param accountNum
	 *            the account num
	 * @param amount
	 *            the amount
	 * @param reply
	 *            the reply
	 */
	private void handleWithdrawOrTransfer(int accountNum, int amount,
			ApplicationReply reply) {
		synchronized (accounts) {
			AccountSnapshot accountSnapshot = accounts
					.getAccountSnapshot(accountNum);
			float balance;
			reply.setAccountNum(accountNum);
			if (accountSnapshot != null) {
				balance = accountSnapshot.getBalance();
				if (balance >= amount) {
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

	/**
	 * Checks if is inconsistent with history.
	 *
	 * @param request
	 *            the request
	 * @return true, if is inconsistent with history
	 */
	private boolean isInconsistentWithHistory(Request request) {
		final ApplicationRequest applicationRequest = (ApplicationRequest) request;
		final ApplicationRequest existingRequest = (ApplicationRequest) chainReplicationMessageHandler
				.getHistoryOfRequests().getExisistingRequest(
						new ApplicationRequestKey(applicationRequest
								.getRequestId(), applicationRequest
								.getAccountNum()));
		if (applicationRequest.equals(existingRequest)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * async.chainreplication.server.IApplicationRequestHandler#updateHistories
	 * (async.chainreplication.client.server.communication.models.Request,
	 * async.chainreplication.client.server.communication.models.Reply)
	 */
	@Override
	public void updateHistories(Request request, Reply reply) {
		final RequestKey requestKey = new ApplicationRequestKey(
				request.getRequestId(),
				((ApplicationRequest) request).getAccountNum());
		chainReplicationMessageHandler.getHistoryOfRequests().addToHistory(
				requestKey, request, reply);
		chainReplicationMessageHandler.getSentHistory().addToSentHistory(
				requestKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see async.chainreplication.server.IApplicationRequestHandler#
	 * updateTransactionalObject(java.lang.Object)
	 */
	@Override
	public void updateTransactionalObject(Object transactionalObject) {
		final AccountSnapshot snapshot = (AccountSnapshot) transactionalObject;
		accounts.updateAccountSnapshot(snapshot.getAccountNum(),
				snapshot.getBalance());
	}

}
