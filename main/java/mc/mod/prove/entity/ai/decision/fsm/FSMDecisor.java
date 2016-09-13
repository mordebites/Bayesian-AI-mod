package mc.mod.prove.entity.ai.decision.fsm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import mc.mod.prove.entity.ai.decision.IDecisor;
import mc.mod.prove.entity.transfer.EvidenceTO;

public class FSMDecisor implements IDecisor {
	FSMState currentState = FSMFactory.getState(FSMFactory.LOOK_AROUND);
	
	//benchmarking
	public long elapsedSum = 0;
	public int repetitions = 0;
	private ThreadMXBean threadMXB;
	
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
