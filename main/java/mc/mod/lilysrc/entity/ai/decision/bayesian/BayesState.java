package mc.mod.lilysrc.entity.ai.decision.bayesian; 
/**
 * Implements one of the possible states for the NPC in the network.
 */
public class BayesState implements Comparable<BayesState> {
	private String name;
	//represents the probability of the state
	private int value;
	
	public BayesState(String name, double value) {
		super();
		this.name = name;
		this.value = (int) Math.floor(value*100) + 1;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public int compareTo(BayesState otherState) {
		return otherState.value - this.value;
	}
}
