package mc.mod.lilysrc.entity.ai.decision.fsm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import mc.mod.lilysrc.entity.ai.decision.Decisor;
import mc.mod.lilysrc.entity.transfer.EvidenceTO;

/**
 * Class implementing the decisor based on the finite state machine.
 */
public class FSMDecisor extends Decisor {
	FSMState currentState = FSMFactory.getState(FSMFactory.LOOK_AROUND);
	
	public FSMDecisor() {
		super();
	}
	
	public String getDecision(EvidenceTO evidence) {
		long start = threadMXB.getCurrentThreadUserTime();
		
		//actual transition
		String result = currentState.transition(evidence);
		currentState = FSMFactory.getState(result);
		
		long elapsed = threadMXB.getCurrentThreadUserTime() - start;
		this.elapsedSum += elapsed;
		repetitions++;
		
		
		return result;
	}
}
