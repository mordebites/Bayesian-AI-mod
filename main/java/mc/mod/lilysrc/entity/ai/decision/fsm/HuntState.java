package mc.mod.lilysrc.entity.ai.decision.fsm;

import mc.mod.lilysrc.entity.transfer.EvidenceTO;

/**
 * Class implementing the Hunt state for the finite state machine.
 */
public class HuntState extends FSMState {

	@Override
	String transition(EvidenceTO evidence) {
		String next = super.transition(evidence);
		
		if (evidence.getTimer().compareTo("Normal") == 0) {
			if (evidence.getPlayerInSight().compareTo("None") != 0) {
				next = "Hunt";
			} else if (evidence.getStepSound().compareTo("Close") == 0) {
				next = "Suspect";
			}
		} else {
			if (evidence.getPlayerInSight().compareTo("None") != 0) {
				next = "Flee";
			}
		}
		
		return next;
	}

}
