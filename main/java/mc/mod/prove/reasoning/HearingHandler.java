package mc.mod.prove.reasoning;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class HearingHandler {
	private EntityCreature entity;
	private final int HEARING_THRESHOLD = 10;

	public HearingHandler(EntityCreature entity) {
		this.entity = entity;
	}

	//controlla se è percepito, e a che distanza, il suono dell'ultimo blocco sonoro attivato
	public EntityDistance checkBlockSound(BlockEvent lastSound, int distanceThreshold){
		EntityDistance blockSound = EntityDistance.None;
		
		if (lastSound != null && lastSound.getTimer()> 0) {
			Vec3d lastSoundPos = new Vec3d(lastSound.getPos());
			blockSound = checkSound(lastSoundPos, distanceThreshold);
			if(blockSound != EntityDistance.None) {
				lastSound.setPerceived(true);
			}
		}
		return blockSound;
	}
	
	//controlla se è percepito, e a che distanza, l'ultimo rumore dei passi prodotto
	public EntityDistance checkStepSound(EntityPlayer player, int distanceThreshold){
		EntityDistance stepSound = EntityDistance.None;
		
		if(!player.isSneaking() ){
			Vec3d playerPos = new Vec3d(player.posX, player.posY, player.posZ);
			stepSound = checkSound(playerPos, distanceThreshold);
		}
		return stepSound;
	}
	
	//se il suono non è vicino, sarà colto solo in caso di assenza di ostacoli tra l'NPC e la fonte
	private EntityDistance checkSound(Vec3d targetPosition, int distanceThreshold) {
		EntityDistance blockSound = EntityDistance.None;
		double distance = entity.getPositionVector().distanceTo(targetPosition);
		
		//se l'npc si sta muovendo il rumore dei propri passi copre la capacità di sentire l'altro
		if(entity.motionX == 0 && entity.motionZ == 0) {
			//se il target è vicino (vedi DISTANCE_THRESHOLD), viene sentito a prescindere
			if(distance <= distanceThreshold){
				blockSound = EntityDistance.Close;
			} else {
				//se il target è lontano
				if (distance <= HEARING_THRESHOLD) {
					blockSound = EntityDistance.Far;
				}
			}
		}
		return blockSound;
	}
}
