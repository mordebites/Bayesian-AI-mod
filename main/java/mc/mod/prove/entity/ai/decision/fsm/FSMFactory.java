package mc.mod.prove.entity.ai.decision.fsm;


public class FSMFactory {
	static final String LOOK_AROUND = "LookAround";
	static final String HUNT = "Hunt";
	static final String SUSPECT = "Suspect";
	static final String TRICK = "Trick";
	static final String FLEE = "Flee";
	private static FSMState lookAround = new LookAroundState();
	private static FSMState hunt = new HuntState();
	private static FSMState suspect = new SuspectState();
	private static FSMState trick = new TrickState();
	private static FSMState flee = new FleeState();
	
	public static FSMState getState(String state) {
		FSMState result = null;
		
		if (state.compareTo(LOOK_AROUND) == 0) {
			result = lookAround;
		} else if (state.compareTo(HUNT) == 0) {
			result = hunt;
		} else if (state.compareTo(SUSPECT) == 0) {
			result = suspect;
		} else if (state.compareTo(TRICK) == 0) {
			result = trick;
		} else if (state.compareTo(FLEE) == 0) {
			result = flee;
		}
		
		return result;
	}

}
