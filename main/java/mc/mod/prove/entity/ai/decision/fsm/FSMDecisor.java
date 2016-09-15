package mc.mod.prove.entity.ai.decision.fsm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import mc.mod.prove.entity.ai.decision.Decisor;
import mc.mod.prove.entity.transfer.EvidenceTO;

public class FSMDecisor extends Decisor {
	FSMState currentState = FSMFactory.getState(FSMFactory.LOOK_AROUND);
	
	public FSMDecisor() {
		threadMXB = ManagementFactory.getThreadMXBean();
        if (!threadMXB.isCurrentThreadCpuTimeSupported())
        {
            System.out.println("thread monitoring not supported by this JVM");
            System.exit(1);
        }
	}
	
	public String getDecision(EvidenceTO evidence) {
		long start = threadMXB.getCurrentThreadUserTime();
		
		String result = currentState.transition(evidence);
		currentState = FSMFactory.getState(result);
		
		
		long elapsed = threadMXB.getCurrentThreadUserTime() - start;
		this.elapsedSum += elapsed;
		repetitions++;
		
		
		return result;
	}
}
