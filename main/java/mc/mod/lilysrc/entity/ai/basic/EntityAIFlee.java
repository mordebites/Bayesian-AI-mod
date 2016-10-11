package mc.mod.lilysrc.entity.ai.basic;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

/**
 * Class that implements the AI for the escape
 */
public class EntityAIFlee extends EntityAIBase {
	private static final int DISTANCE_THRESHOLD = 6;
	private EntityCreature entity;
	private EntityPlayer player;
	private double speed;
	private double newPosX;
	private double newPosY;
	private double newPosZ;
	
	public EntityAIFlee(EntityCreature entity, EntityPlayer player, double speed) {
		this.entity = entity;
		this.player = player;
		this.speed = speed;
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public void startExecuting(){
		position();
	}
	
	@Override
	public boolean continueExecuting() {
		//if the entity has reached the chosen spot
		//or the opponent is closer than a certain threshold
		//then a new destination is chosen 
		if((entity.getPositionVector().equals(new Vec3d(newPosX, newPosY, newPosZ)) && 
			player.getPositionVector().distanceTo(entity.getPositionVector()) < DISTANCE_THRESHOLD)
			|| (entity.motionX == 0 && entity.motionZ == 0)){
			position();
		}

		return !entity.getNavigator().noPath();
	}

	public void position(){
		Vec3d vec3 = null;
		boolean invisible = false;
		do {
			vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 10, 0, player.getPositionVector());
			if (vec3 != null && entity.worldObj.rayTraceBlocks(
					vec3, player.getPositionVector(), false, true, false) != null) {
				invisible = true;
			}
		//the chosen position must not be visible from player's position
		} while (vec3 == null && !invisible);
		
		newPosX = vec3.xCoord;
		newPosY = vec3.yCoord;
		newPosZ = vec3.zCoord;
		
		this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);
	}
}
