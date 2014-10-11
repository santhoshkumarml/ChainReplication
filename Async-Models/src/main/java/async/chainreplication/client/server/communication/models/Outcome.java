package async.chainreplication.client.server.communication.models;

public enum Outcome {
	Processed,
	InconsistentWithHistory,
	InsufficientFunds;
}
