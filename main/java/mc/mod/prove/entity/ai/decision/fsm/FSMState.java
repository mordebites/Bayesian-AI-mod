package mc.mod.prove.entity.ai.decision.fsm;

import mc.mod.prove.entity.transfer.EvidenceTO;

public abstract class FSMState {
	String name;
	
	String transition(EvidenceTO evidence) {
		return "LookAround";
	}
	
}
