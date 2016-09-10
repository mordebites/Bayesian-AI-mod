package mc.mod.prove.entity.ai.decision;

import mc.mod.prove.entity.transfer.EvidenceTO;

public interface IDecisor {
	public String getDecision(EvidenceTO evidence);
}
