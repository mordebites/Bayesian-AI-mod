package mc.mod.prove.entity.ai.basic;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityAIHunt extends EntityAIBase {
	private EntityCreature entity;
	private EntityPlayer player;
	private double speed;
	
	public EntityAIHunt(EntityCreature entity, EntityPlayer player, double speed) {
		this.entity = entity;
		this.player = player;
		this.speed = speed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {		
		return true;
	}
	
	@Override
	public void startExecuting(){
		this.entity.getNavigator().tryMoveToEntityLiving(player, speed);
	}
	
	@Override
	public boolean continueExecuting() {
		return !entity.getNavigator().noPath();
	}

}
