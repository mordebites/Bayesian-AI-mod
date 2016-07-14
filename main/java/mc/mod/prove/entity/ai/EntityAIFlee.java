package mc.mod.prove.entity.ai;

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
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		System.out.println("Should flee!");
		return true;
		/*if (!theEntityCreature.canEntityBeSeen(thePlayer)) {
			return false;
		} else {
			Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theEntityCreature, 5, 0, thePlayer.getPositionVector());
			if (vec3 == null) {
				return false;
			} else {
				newPosX = vec3.xCoord;
				newPosY = vec3.yCoord;
				newPosZ = vec3.zCoord;
				return true;
			}
		}*/
	}
	
	@Override
	public void startExecuting(){
		System.out.println("Starting!");
		position();
		/*this.theEntityCreature.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);*/
	}
	
	@Override
	public boolean continueExecuting() {
		System.out.println("Continuing!");
		if(player.getPositionVector().distanceTo(entity.getPositionVector()) < 5 && 
				(entity.getPositionVector().equals(new Vec3d(newPosX, newPosY, newPosZ)))){
			position();
		}

		return !entity.getNavigator().noPath();
	}

	public void position(){
		Vec3d vec3 = null;
		do {
			vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 10, 0, player.getPositionVector());
		} while (vec3 == null);
		
		newPosX = vec3.xCoord;
		newPosY = vec3.yCoord;
		newPosZ = vec3.zCoord;
		
		this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);
	}
}
