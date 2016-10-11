package mc.mod.lilysrc.entity.ai.decision.bayesian;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.PriorityQueue;
import java.util.Random;

import mc.mod.lilysrc.entity.ai.decision.Decisor;
import mc.mod.lilysrc.entity.transfer.EvidenceTO;
import smile.Network;

/**
 * Class implementing the decisor based on the bayesian network.
 */
public class BayesianDecisor extends Decisor{
	private Network net = new Network();
	//index of the State t+1 inside the network
	private int stateT1;
	//previous statet+1
	private String exStatet1 = "LookAround";
	private String[] outcomes;
	private PriorityQueue<BayesState> decision = new PriorityQueue<BayesState>();
	//checks whether the net is updated
	private boolean updated = false;
	private Random rdm = new Random();
	
	public BayesianDecisor() {
		super();
		//the file with the net has been converted into a long string to overcome compatibility problems
		net.readString(FilerXDSL.NET);
		stateT1 = net.getNode("State_t1");
		outcomes = net.getOutcomeIds(stateT1);
		net.updateBeliefs();
	}

	/**
	 * Sets the data to update net's beliefs.
	 * 
	 * @param evidence
	 *            transfer object containing data
	 */
	private void setEvidence(EvidenceTO evidence) {
		net.setEvidence("State_t", exStatet1);
		net.setEvidence("Timer", evidence.getTimer());
		net.setEvidence("Player_In_Sight", evidence.getPlayerInSight());
		net.setEvidence("Step_Sound", evidence.getStepSound());
		net.setEvidence("Lighting_Change", evidence.getLightingChange());
		net.setEvidence("Sound_Block", evidence.getBlockSound());

		net.updateBeliefs();
		updated = true;
	}

	/**
	 * Performs bayesian reasoning and returns the name of the state to execute.
	 * 
	 * @return a string with the name of the state to execute
	 */
	public String getDecision(EvidenceTO evidence) {
		long start = threadMXB.getCurrentThreadUserTime();
		
		this.setEvidence(evidence);
		double[] value = net.getNodeValue(stateT1);
		for (int i = 0; i < outcomes.length; i++) {
			BayesState currentState = new BayesState(outcomes[i], value[i]);
			decision.add(currentState);
		}

		BayesState[] states = new BayesState[3];
		for (int i = 0; i < 3; i++) {
			states[i] = decision.poll();
		}
		BayesState finalDecision = makeDecision(states);

		updated = false;
		exStatet1 = finalDecision.getName();

		String finale = finalDecision.getName();

		long elapsed = threadMXB.getCurrentThreadUserTime() - start;
		this.elapsedSum += elapsed;
		repetitions++;
		return finale;
	}

	//introduces nondeterminism
	private BayesState makeDecision(BayesState[] states) {
		BayesState finalDecision = states[0];
		final int THRESHOLD = 5;

		// if first option had probability close to second option's
		if (states[0].getValue() - states[1].getValue() <= THRESHOLD) {
			int choices = 2;
			// if third option had probability close to first options'
			if (states[1].getValue() - states[2].getValue() <= THRESHOLD) {
				choices = 3;
			}

			int totalWeight = 0; // this stores sum of weights of all elements
									// before current
			for (int i = 0; i < choices; i++) {
				int weight = states[i].getValue(); // weight of current element
				int r = rdm.nextInt(totalWeight + weight);// random value
				if (r >= totalWeight) // probability of this is
										// weight/(totalWeight+weight)
					finalDecision = states[i]; // it is the probability of
												// discarding last selected
												// element and selecting current
												// one instead
				totalWeight += weight; // increase weight sum
			}
		}
		return finalDecision;
	}
}
