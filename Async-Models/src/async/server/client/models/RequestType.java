package async.server.client.models;

public enum RequestType {
	GET_BALANCE(QueryOrUpdate.QUERY),
	DEPOSIT(QueryOrUpdate.UPDATE),
	TRANSFER(QueryOrUpdate.UPDATE),
	WITHDRAW(QueryOrUpdate.UPDATE);
	
	private QueryOrUpdate queryOrUpdate;

	RequestType(QueryOrUpdate queryOrUpdate) {
		this.queryOrUpdate = queryOrUpdate;
	}

	public QueryOrUpdate getQueryOrUpdate() {
		return queryOrUpdate;
	}
}
