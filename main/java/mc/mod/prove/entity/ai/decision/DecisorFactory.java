package mc.mod.prove.entity.ai.decision;

import mc.mod.prove.entity.ai.decision.fsm.FSMDecisor;
import mc.mod.prove.entity.ai.decision.tree.TreeDecisor;
import mc.mod.prove.entity.decision.bayesian.BayesianDecisor;

public class DecisorFactory {
	public static final int BAYES_AGGRESSIVE = 0;
	public static final int FSM_AGGRESSIVE = 1;
	public static final int TREE_AGGRESSIVE = 2;
	
	public static Decisor getDecisor(int type) {
		Decisor decisor = null;
		switch (type) {
			case BAYES_AGGRESSIVE : {
				decisor = new BayesianDecisor();
				break;
			}
			case FSM_AGGRESSIVE : {
				decisor = new FSMDecisor();
				break;
			}
			case TREE_AGGRESSIVE : {
				decisor = new TreeDecisor();
				break;
			}
			default : {
				RuntimeException e = new RuntimeException();
				throw new RuntimeException();
			}	
		}
		return decisor;
	}

}
