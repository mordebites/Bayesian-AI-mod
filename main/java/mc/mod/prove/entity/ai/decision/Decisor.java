package mc.mod.prove.entity.ai.decision;

import java.lang.management.ThreadMXBean;

import mc.mod.prove.entity.transfer.EvidenceTO;

public abstract class Decisor {
	
	//benchmarking
	public long elapsedSum = 0;
	public int repetitions = 0;
	protected ThreadMXBean threadMXB;
	
	public abstract String getDecision(EvidenceTO evidence);
}
