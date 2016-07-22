package mc.mod.prove.entity.bayesian;

import java.util.PriorityQueue;
import java.util.Random;

import mc.mod.prove.entity.ai.EntityAIFactory;
import mc.mod.prove.entity.transfer.EvidenceTO;
import smile.Network;

public class BayesianHandler {
	//TODO rendere relativo il path e inserire il file nella cartella del progetto
	private static final String NAME = "C:/Users/Abbattista/Desktop/De Liddo/Lily Bayes/lily prove - mejo.xdsl";
	private Network net = new Network();
	private int stateT1;
	private String exStatet1 = "LookAround";
	private String[] esiti;
	private PriorityQueue<State> decision = new PriorityQueue<State>();
	private boolean updated = false;
	private Random rdm = new Random();
	
	/*	
	public static void main(String[] args) {
		BayesianHandler handler = new BayesianHandler();
		EvidenceTO ev = new EvidenceTO("None", "Normal", "None",
										"Close", "None", "Unlikely");
		handler.setEvidence(ev);
		System.out.println(handler.getDecision());
	}
	 */
	public BayesianHandler() {
		net.readFile(NAME);
		stateT1 = net.getNode("State_t1");
		esiti = net.getOutcomeIds(stateT1);
		net.updateBeliefs();
	}

	/**
	 * Permette di impostare i risultati delle osservazioni in modo da aggiornare la rete.
	 * 
	 * @param evidence il transfer object contenente i risultati delle osservazioni
	 */
	public void setEvidence(EvidenceTO evidence){
		net.setEvidence("State_t", exStatet1);
		net.setEvidence("Timer", evidence.getTimer());
		net.setEvidence("Player_In_Sight", evidence.getPlayerInSight());
		net.setEvidence("Step_Sound", evidence.getStepSound());
		net.setEvidence("Lighting_Change", evidence.getLightingChange());
		net.setEvidence("Sound_Block", evidence.getBlockSound());
		net.setEvidence("Player_Tricking", evidence.getPlayerTricking());
		
		net.updateBeliefs();
		updated = true;
	}
	
	/**
	 * Esegue il ragionamento bayesiano e restituisce il nome dello stato da eseguire.
	 * Il metodo deve essere chiamato dopo aver settato i risultati delle osservazioni.
	 * 
	 * @return la stringa con il nome dello stato da eseguire
	 */
	public String getDecision(){
		if (!updated) {
			throw new RuntimeException("Network Not Updated!");
		}
		double[] value = net.getNodeValue(stateT1);
		for(int i = 0; i < esiti.length; i++) {
			State currentState = new State(esiti[i], value[i]);
			decision.add(currentState);
		}
	
		State[] states = new State[3];
		for(int i = 0; i < 3; i++) {
			states[i] = decision.poll();
		}
		State finalDecision = makeDecision(states);

		updated = false;
		exStatet1 = finalDecision.getName();
		
		String finale = finalDecision.getName();

		//TODO togli Suspect
		return "Suspect";
	}
	
	private State makeDecision(State[] states){
		State finalDecision = states[0];
		final int THRESHOLD = 5;
		
		//se la prima opzione e' abbastanza vicina alla seconda come probabilita'
		if(states[0].getValue() - states[1].getValue() <= THRESHOLD) {
			int choices = 2;
			if(states[1].getValue() - states[2].getValue() <= THRESHOLD) {
				choices = 3;
			}
			
			int totalWeight = 0; // this stores sum of weights of all elements before current
		    for(int i = 0; i < choices; i++){
		    	int weight = states[i].getValue(); // weight of current element
		        int r = rdm.nextInt(totalWeight + weight);// random value
		        if (r >= totalWeight) // probability of this is weight/(totalWeight+weight)
		            finalDecision = states[i]; // it is the probability of discarding last selected element and selecting current one instead
		        totalWeight += weight; // increase weight sum
		    }
		}
		return finalDecision;
	}
}
