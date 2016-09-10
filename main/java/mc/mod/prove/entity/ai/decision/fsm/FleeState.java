package mc.mod.prove.entity.ai.decision.fsm;

import mc.mod.prove.entity.transfer.EvidenceTO;

public class FleeState extends FSMState {

	@Override
	String transition(EvidenceTO evidence) {
		String next = super.transition(evidence);
		
		if (evidence.getPlayerInSight().compareTo("None") != 0
			&& (evidence.getStepSound().compareTo("Close") == 0
			|| evidence.getLightingChange().compareTo("Close") == 0
			|| evidence.getBlockSound().compareTo("Close") == 0)) {
			next = "Flee";
		}
		
		return next;
	}

}
