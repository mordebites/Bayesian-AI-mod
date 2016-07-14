package mc.mod.prove.entity.bayesian; 

public class State implements Comparable<State> {
	private String name;
	private int value;
	
	public State(String name, double value) {
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
	public int compareTo(State otherState) {
		//arrotonda i valori per eccesso e poi fa il casting a int 
		//prima di eseguire la sottrazione
		return otherState.value - this.value;
	}
}
