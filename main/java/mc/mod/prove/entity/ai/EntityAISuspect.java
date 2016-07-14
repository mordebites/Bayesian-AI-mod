package mc.mod.prove.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAISuspect extends EntityAIBase {
	private EntityCreature entity;
	private EntityPlayer player;
	private double speed;
	private double newPosX;
	private double newPosY;
	private double newPosZ;
	private BlockPos lastPlate;
	
	
	public EntityAISuspect(EntityCreature entity, EntityPlayer player, double speed, BlockPos lastPlate) {
		this.entity = entity;
		this.player = player;
		this.speed = speed;
		this.lastPlate = lastPlate;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public void startExecuting(){
		System.out.println("Starting!");
		//TODO TOGLI
		lastPlate = player.getPosition();
		
		//position of the last activated plate
		Vec3d blockVec = new Vec3d(lastPlate.getX(), lastPlate.getY(), lastPlate.getZ());
		//distanza tra l'npc e il blocco attivato
		int distance = (int) entity.getPositionVector().distanceTo(blockVec);
		
		Vec3d vec3 = null;
		do {
			vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(entity, distance, 0, player.getPositionVector());
		} while (vec3 == null);
		
		newPosX = vec3.xCoord;
		newPosY = vec3.yCoord;
		newPosZ = vec3.zCoord;
		
		this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);
	}
	
	@Override
	public boolean continueExecuting() {
		System.out.println("Continuing!");

		return !entity.getNavigator().noPath();
	}
}
