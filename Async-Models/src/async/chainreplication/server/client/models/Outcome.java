package async.chainreplication.server.client.models;

public enum Outcome {
	Processed,
	InconsistentWithHistory,
	InsufficientFunds;
}
