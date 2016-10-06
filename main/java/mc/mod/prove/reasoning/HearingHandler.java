package mc.mod.prove.reasoning;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class HearingHandler {
	private Entity entity;
	private final int HEARING_THRESHOLD = 10;

	public HearingHandler(Entity entity) {
		this.entity = entity;
	}

	//checks if sound from last activated block is perceived and how distant it is
	public EntityDistance checkBlockSound(BlockEvent lastSound, int distanceThreshold){
		EntityDistance blockSound = EntityDistance.None;
		
		if (lastSound != null && lastSound.getTimer()> 0) {
			Vec3d lastSoundPos = new Vec3d(lastSound.getPos());
			blockSound = checkSound(lastSoundPos, distanceThreshold);
		}
		return blockSound;
	}
	
	//checks if last step sound is perceived and how distant it is
	public EntityDistance checkStepSound(Entity player, int distanceThreshold){
		EntityDistance stepSound = EntityDistance.None;
		
		if(!player.isSneaking() && (player.motionX != 0 || player.motionZ != 0)){
			Vec3d playerPos = new Vec3d(player.posX, player.posY, player.posZ);
			stepSound = checkSound(playerPos, distanceThreshold);
		}
		return stepSound;
	}
	
	private EntityDistance checkSound(Vec3d targetPosition, int distanceThreshold) {
		EntityDistance blockSound = EntityDistance.None;
		double distance = entity.getPositionVector().distanceTo(targetPosition);
		
		if(distance <= distanceThreshold){
			blockSound = EntityDistance.Close;
		} else {
			//se target is far
			if (distance <= HEARING_THRESHOLD) {
				blockSound = EntityDistance.Far;
			}
		}
		return blockSound;
	}
}
