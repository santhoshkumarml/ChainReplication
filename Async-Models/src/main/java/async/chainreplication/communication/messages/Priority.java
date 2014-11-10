package async.chainreplication.communication.messages;

// TODO: Auto-generated Javadoc
/**
 * The Enum Priority.
 */
public enum Priority {
	
	/** The normal priority. */
	NORMAL_PRIORITY, // All log messages are normal priority
	/** The above normal priority. */
 ABOVE_NORMAL_PRIORITY, // ACk Messages are above normal priority
	/** The high priority. */
 HIGH_PRIORITY, // Request Messages/Response messages/Sync Messages are high
					// priority
	/** The higher priority. */
 HIGHER_PRIORITY, // All Sequence Message exchange for Successor Change/Bulk
						// Sync Message
	/** The realtime priority. */
 REALTIME_PRIORITY // All Master messages/Heart beat messages are Real time
						// priority
}
