package mc.mod.lilysrc.entity.ai.decision;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import mc.mod.lilysrc.entity.transfer.EvidenceTO;

public abstract class Decisor {
	
	//benchmarking
	public long elapsedSum = 0;
	public int repetitions = 0;
	protected ThreadMXBean threadMXB;
	
	public Decisor() {
		threadMXB = ManagementFactory.getThreadMXBean();
        if (!threadMXB.isCurrentThreadCpuTimeSupported())
        {
            System.out.println("thread monitoring not supported by this JVM");
            System.exit(1);
        }
	}
	
	public abstract String getDecision(EvidenceTO evidence);
}
