package mc.mod.prove.entity.ai.basic;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
	private BlockPos lastPosition = null;
	
	
	public EntityAISuspect(EntityCreature entity, EntityPlayer player, double speed) {
		this.entity = entity;
		this.player = player;
		this.speed = speed;
	}

	public void setPlayerLastPosition(BlockPos lastPosition){
		this.lastPosition = lastPosition;
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public void startExecuting(){
		//controlla
		if(lastPosition == null) {
			System.err.println("Last plate not set!");
		} else {
			//position of the last activated plate
			Vec3d lastPosVec = new Vec3d(lastPosition.getX(), lastPosition.getY(), lastPosition.getZ());
			//distanza tra l'npc e il blocco attivato
			int distance = (int) entity.getPositionVector().distanceTo(lastPosVec);
			
			Vec3d vec3 = null;
			do {
				vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(entity, distance > 0 ? distance : 2, 0, lastPosVec);
			} while (vec3 == null);
			
			newPosX = vec3.xCoord;
			newPosY = vec3.yCoord;
			newPosZ = vec3.zCoord;
			
			this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);
		}
	}
	
	@Override
	public boolean continueExecuting() {
		pathHelper();
		return !entity.getNavigator().noPath();
	}
	
	//TODO sistemami, ricerca in croce
	private void pathHelper() {
		BlockPos pos = new BlockPos(entity.posX+entity.motionX, entity.posY+entity.motionY, entity.posZ+entity.motionZ);
		IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		if(blockState.getBlock() instanceof BlockPressurePlate){
			entity.motionY = 0.42D;
		}
	}
}
