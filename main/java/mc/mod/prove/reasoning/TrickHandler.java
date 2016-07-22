
package mc.mod.prove.reasoning;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.entity.ai.enumerations.Tricking;
import mc.mod.prove.entity.transfer.TrickDeductionTO;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class TrickHandler {
	
	public Tricking isPlayerTricking(TrickDeductionTO to){
		Tricking prob = Tricking.Uncertain;
		
		//Se il giocatore non è in vista
		if(to.getPlayerInSight() == EntityDistance.None) {
			//Se è stato attivato un blocco sonoro o luminoso
			if ((to.getBlockSound() != EntityDistance.None) || (to.getLightChange() != EntityDistance.None)){
				//se non si sente rumore di passi, si ipotizza che il giocatore stia tendendo un agguato
				if (to.getStepSound() == EntityDistance.None){
					prob = Tricking.Likely;
				} else {
					BlockEvent light = to.getLastLight();
					BlockEvent sound = to.getLastSound();
					EntityPlayer player = to.getPlayer();
					
					//se la luce è accesa e il giocatore si sta allontanando da essa
					if(to.getLightChange() != EntityDistance.None && player.getPositionVector().distanceTo(new Vec3d(light.getPos())) >
						(new Vec3d(player.prevPosX, player.prevPosY, player.prevPosZ)).distanceTo(new Vec3d(light.getPos()))){
						
						prob = Tricking.Unlikely;
					//se è stato attivato un blocco sonoro e il giocatore si sta allontanando da esso
					} else if (to.getBlockSound() != EntityDistance.None && player.getPositionVector().distanceTo(new Vec3d(sound.getPos())) >
						(new Vec3d(player.prevPosX, player.prevPosY, player.prevPosZ)).distanceTo(new Vec3d(sound.getPos()))){
						
						prob = Tricking.Unlikely;
					//se il giocatore si sta muovendo verso uno dei due blocchi	
					} else {
						prob = Tricking.Likely;
					}
				}
			}
		// se il giocatore è in vista
		} else {
			prob = Tricking.Unlikely;
		}
		
		return prob;
	}
}
