package mc.mod.lilysrc.entity.ai.basic;

import java.util.Iterator;
import java.util.Random;

import mc.mod.lilysrc.MainRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAITrick extends EntityAIBase {
	private EntityCreature entity;
	private Vec3d lastPosition = null;
	private double speed;
	private Iterator[] iters = new Iterator[2];
	private Random rdm = new Random();
	private boolean tricking = false;
	//fake entity to have the main entity face a certain position 
	private EntityLiving fakeRotationEntity = new EntityCow(Minecraft.getMinecraft().theWorld);
	
	//vectors
	private Vec3d plateToPress = null;
	private Vec3d ambushPlace = null;
	
	//position flags
	private boolean platePressed = false; //shows if the plate chosen to trick was pressed
	private boolean movingToPlate = false; //shows if entity is moving to reach the plate
	private boolean ambushPlaceChosen = false; //shows if the entity chose an ambush place
	
	private int stopTimer = 0;
	
	public boolean isTricking() {
		return tricking;
	}

	public void setTricking() {
		this.tricking = true;
	}

	public EntityAITrick(EntityCreature entity,	double speed) {
		this.entity = entity;
		this.speed = speed;
		this.entity.setSprinting(false);
	}
	

	public void setIterators(Iterator lights, Iterator sounds) {		
		int no = rdm.nextInt(2) + 1;
		if(no == 1){
			iters[0] = lights;
			iters[1] = sounds;
		} else {
			iters[0] = sounds;
			iters[1] = lights;
		}	
	}
	
	public void setPlayerLastPosition(BlockPos lastPosition) {
		//last position unknown
		if (lastPosition != null) {
			this.lastPosition =  new Vec3d(lastPosition.getX(), lastPosition.getY(), lastPosition.getZ());
		} else if (plateToPress == null) {
			int pos = rdm.nextInt(4);
			int i = 0;
			//chooses a plate to press among all the plates
			while (plateToPress == null) {
				if(iters[0].hasNext()){
					if(i == pos) {
						BlockPos b = (BlockPos) iters[0].next();
						plateToPress = new Vec3d(b.getX(), b.getY(), b.getZ());
					}
				} else if(iters[1].hasNext()) {
					if(i == pos) {
						BlockPos b = (BlockPos) iters[1].next();
						plateToPress = new Vec3d(b.getX(), b.getY(), b.getZ());
					}
				}			
				i++;
			}
		}
	}
	
	

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void startExecuting(){
		//block to press not yet chosen
		if (plateToPress == null) {
			BlockPos a = null;
			int i = 0;
			
			while (plateToPress == null && i < 2) {
				Iterator itr = iters[i];
				while (itr.hasNext()) {
					a = ((BlockPos) itr.next());
					Vec3d actual = new Vec3d(a.getX(), a.getY(), a.getZ());
					if (actual.distanceTo(lastPosition) >= 7) {
						plateToPress = actual;
					}
				}
				i++;
			}
		}
	}
	
	@Override
	public boolean continueExecuting() {
		if(!movingToPlate) {
			if(plateToPress == null) {
				this.setPlayerLastPosition(null);
			}
			if (this.entity.getNavigator().tryMoveToXYZ(plateToPress.xCoord, plateToPress.yCoord, 
					plateToPress.zCoord, speed)) {
				movingToPlate = true;
			}
		} else {
			if(!platePressed){
				if (entity.getPosition().getX() == MathHelper.floor_double(plateToPress.xCoord) &&
					entity.getPosition().getZ() == MathHelper.floor_double(plateToPress.zCoord)) {
					platePressed = true;
					
					//if the entity is not moving, it tries to set again the path navigator
				} else if (entity.getNavigator().getPath() == null || (entity.motionX == 0 && entity.motionZ == 0)) {
					this.entity.getNavigator().tryMoveToXYZ(plateToPress.xCoord, plateToPress.yCoord, 
							plateToPress.zCoord, speed);
				}
			} else {
				if(ambushPlace == null) {
					Vec3d vec3 = null;
					boolean invisible = false;
					do {
						vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 4, 0, plateToPress);
						if(vec3 != null) {
							//if position is inside the labyrinth
							if (vec3.xCoord >= MainRegistry.MIN_X_LAB && vec3.xCoord <= MainRegistry.MAX_X_LAB &&
								vec3.zCoord >= MainRegistry.MIN_Z_LAB && vec3.zCoord <= MainRegistry.MAX_Z_LAB) {
								//checks if block is visible from current position
								if(entity.worldObj.rayTraceBlocks(plateToPress, vec3, false, true, false) != null) {
									invisible = true;
								}
							}
						}
					} while (vec3 == null || !invisible);
					
					double newPosX = vec3.xCoord;
					double newPosY = vec3.yCoord;
					double newPosZ = vec3.zCoord;
					
					if (this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed)) {
						ambushPlace = vec3;
					}
				} else {
					//if has reached ambush place
					if(entity.getPosition().getX() == MathHelper.floor_double(ambushPlace.xCoord) &&
							entity.getPosition().getZ() == MathHelper.floor_double(ambushPlace.zCoord)) {
						entity.getNavigator().clearPathEntity();
						
						if (lastPosition != null) {
							fakeRotationEntity.setPosition(lastPosition.xCoord, lastPosition.yCoord, lastPosition.zCoord);
							entity.faceEntity(fakeRotationEntity, 360F, 360F);
						}
						resetVariables();
						
						//if entity is not moving
					} else if (entity.getNavigator().getPath() == null || (entity.motionX == 0 && entity.motionZ == 0)) {
						this.entity.getNavigator().tryMoveToXYZ(ambushPlace.xCoord, ambushPlace.yCoord, 
								ambushPlace.zCoord, speed);
					}
				}
			} 
		}
		return !entity.getNavigator().noPath();
	}
	
	
	private void resetVariables() {
		platePressed = false;
		movingToPlate = false;
		ambushPlaceChosen = false;
		
		plateToPress = null;
		ambushPlace = null;
		tricking = false;
	}
}