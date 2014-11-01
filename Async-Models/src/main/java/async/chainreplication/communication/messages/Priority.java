package async.chainreplication.communication.messages;

public enum Priority {
	NORMAL_PRIORITY, //All log messages are normal priority
	ABOVE_NORMAL_PRIORITY, //ACk Messages are above normal priority
	HIGH_PRIORITY, // Request Messages/Response messages/Sync Messages are high priority
	REALTIME_PRIORITY //All Master messages/Heart beat messages are Real time priority
}
