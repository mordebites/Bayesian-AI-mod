
package mc.mod.prove.reasoning;

import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.entity.ai.enumerations.Tricking;
import mc.mod.prove.entity.bayesian.State;
import mc.mod.prove.entity.transfer.TrickDeductionTO;

public class TrickHandler {
	
	public Tricking isPlayerTricking(TrickDeductionTO to){
		Tricking prob = Tricking.Uncertain;
		
		//Se è stato attivato un blocco sonoro o luminoso
		if ((to.getBlockSound() != EntityDistance.None) || (to.getLightChange() != EntityDistance.None)){
			//se non si sente rumore di passi, si ipotizza che il giocatore stia tendendo un agguato
			if (to.getStepSound() == EntityDistance.None){
				prob = Tricking.Likely;
			} else {
				prob = Tricking.Unlikely;
			}
		}
		return prob;
	}
	
	
	public Tricking isOtherPlayerTricking(TrickDeductionTO to, State stateT1) {
		Tricking prob = Tricking.Uncertain;
		
		//Se è stato attivato un blocco sonoro o luminoso
		if ((to.getBlockSound() != EntityDistance.None) || (to.getLightChange() != EntityDistance.None)){
			
		}
		
		return prob;
	}
}
