package mc.mod.prove.entity.ai.basic;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityAIFlee extends EntityAIBase {
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
		/*this.theEntityCreature.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);*/
	}
	
	@Override
	public boolean continueExecuting() {
		if((entity.getPositionVector().equals(new Vec3d(newPosX, newPosY, newPosZ)) && 
			player.getPositionVector().distanceTo(entity.getPositionVector()) < 3)
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
		} while (vec3 == null && !invisible);
		
		newPosX = vec3.xCoord;
		newPosY = vec3.yCoord;
		newPosZ = vec3.zCoord;
		
		this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);
	}
}
