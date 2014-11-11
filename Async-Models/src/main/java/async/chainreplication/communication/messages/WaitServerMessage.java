package async.chainreplication.communication.messages;

import java.util.HashSet;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class WaitServerMessage.
 */
public class WaitServerMessage extends ChainReplicationMessage{

	/** The waiting class. */
	Set<Class<?>> waitingClasses = new HashSet<Class<?>>();

	/** The priorities. */
	Set<Priority> priorities = new HashSet<Priority>();

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1371867708397244804L;

	/**
	 * Instantiates a new wait server message.
	 *
	 * @param waitingClasses the waiting classes
	 * @param priorities the priorities
	 */
	public WaitServerMessage(Set<Class<?>> waitingClasses, Set<Priority> priorities) {
		super(Priority.REALTIME_PRIORITY);
		if(waitingClasses != null) {
			this.waitingClasses.addAll(waitingClasses);
		}
		if(priorities != null) {
			this.priorities.addAll(priorities);
		}
		assert ((this.waitingClasses!=null&&!this.waitingClasses.isEmpty()) ||
				(this.priorities!=null&&!this.priorities.isEmpty()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	/**
	 * Check.
	 *
	 * @param chainReplicationMessageInstance the chain replication message instance
	 * @return true, if successful
	 */
	public boolean check(ChainReplicationMessage chainReplicationMessageInstance) {
		boolean isWaitingClassConditionSatisified = false;
		if(this.waitingClasses != null) {
			isWaitingClassConditionSatisified = this.waitingClasses.contains(
					chainReplicationMessageInstance.getClass());
		}
		if(!isWaitingClassConditionSatisified) {
			if(this.priority!=null) {
				isWaitingClassConditionSatisified = this.priorities.contains(
						chainReplicationMessageInstance.getPriority()); 
			}
		}
		return isWaitingClassConditionSatisified;
	}

}
