package mc.mod.prove.entity.decision.bayesian; 

public class BayesState implements Comparable<BayesState> {
	private String name;
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
		//arrotonda i valori per eccesso e poi fa il casting a int 
		//prima di eseguire la sottrazione
		return otherState.value - this.value;
	}
}
