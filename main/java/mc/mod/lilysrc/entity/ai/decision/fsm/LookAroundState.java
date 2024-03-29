package mc.mod.lilysrc.entity.ai.decision.fsm;
import java.util.Random;

import mc.mod.lilysrc.entity.transfer.EvidenceTO;

/**
 * Class implementing the LookAround state for the finite state machine.
 */
public class LookAroundState extends HuntState{
	private Random rdm = new Random();
	//used to ensure nondeterminism
	private String[] nonDetermin = {"LookAround", "Trick", "Flee"};

	@Override
	String transition(EvidenceTO evidence) {
		String next = super.transition(evidence);
		int nextRandom = 2;
		
		if (evidence.getTimer().compareTo("RunningOut") == 0) {
			if (evidence.getPlayerInSight().compareTo("None") == 0
				&& evidence.getStepSound().compareTo("Close") == 0) {
				next = "Flee";
				
			} else if (evidence.getLightingChange().compareTo("Close") == 0
					    || evidence.getBlockSound().compareTo("Close") == 0) {
				next = "LookAround";
				nextRandom = 3;
			}
		}
		
		//non deterministic
		if (next.compareTo("LookAround") == 0) {
			int n = rdm.nextInt(nextRandom);
			next = nonDetermin[n];
		}
		
		return next;
	}
}
