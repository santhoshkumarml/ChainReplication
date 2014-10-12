package async.chainreplication.application.models;

public enum Outcome {
	Processed,
	InconsistentWithHistory,
	InsufficientFunds;
}
