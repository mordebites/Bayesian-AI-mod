package mc.mod.lilysrc.entity.ai.decision.fsm;

import mc.mod.lilysrc.entity.transfer.EvidenceTO;

public abstract class FSMState {
	String name;
	
	String transition(EvidenceTO evidence) {
		return "LookAround";
	}
	
}
