package mc.mod.prove.entity.ai.decision.fsm;

import mc.mod.prove.entity.ai.decision.IDecisor;
import mc.mod.prove.entity.transfer.EvidenceTO;

public class FSMDecisor implements IDecisor {
	FSMState currentState = FSMFactory.getState(FSMFactory.LOOK_AROUND);

	public String getDecision(EvidenceTO evidence) {
		String result = currentState.transition(evidence);
		currentState = FSMFactory.getState(result);
		return result;
	}
}
