package mc.mod.prove.entity.ai.basic;

import java.util.Iterator;
import java.util.Random;

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
	private int[] labLimits;
	private boolean tricking = false;
	private EntityLiving fakeRotationEntity = new EntityCow(Minecraft.getMinecraft().theWorld);
	
	//vectors
	private Vec3d plateToPress = null;
	private Vec3d ambushPlace = null;
	
	//position flags
	private boolean platePressed = false; //shows if the plate chosen to trick was pressed
	private boolean movingToPlate = false;
	private boolean ambushPlaceChosen = false;
	
	private int stopTimer = 0;
	
	public boolean isTricking() {
		return tricking;
	}

	public void setTricking() {
		this.tricking = true;
	}

	public EntityAITrick(EntityCreature entity,	double speed, int[] labLimits) {
		this.entity = entity;
		this.speed = speed;
		this.labLimits = labLimits;
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
		//se non è nota l'ultima posizione
		if (lastPosition != null) {
			this.lastPosition =  new Vec3d(lastPosition.getX(), lastPosition.getY(), lastPosition.getZ());
		} else if (plateToPress == null) {
			int pos = rdm.nextInt(4);
			int i = 0;
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
 			System.out.println("Random plate to press: " + plateToPress.toString());
		}
	}
	
	

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void startExecuting(){
		//se non ha raggiunto il blocco da premere
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
						System.out.println("Plate to press: " + plateToPress.toString());
					}
				}
				i++;
			}
		}
	}
	
	@Override
	public boolean continueExecuting() {
		if(!movingToPlate) {
			System.out.println("Trying to move to plate!");
			if(plateToPress == null) {
				this.setPlayerLastPosition(null);
			}
			if (this.entity.getNavigator().tryMoveToXYZ(plateToPress.xCoord, plateToPress.yCoord, 
					plateToPress.zCoord, speed)) {
				movingToPlate = true;
				System.out.println("Moving to plate!");
			}
		} else {
			if(!platePressed){
				if (entity.getPosition().getX() == MathHelper.floor_double(plateToPress.xCoord) &&
					entity.getPosition().getZ() == MathHelper.floor_double(plateToPress.zCoord)) {
					System.out.println("Plate pressed!");
					platePressed = true;
				} else if (entity.getNavigator().getPath() == null || (entity.motionX == 0 && entity.motionZ == 0)) {
					this.entity.getNavigator().tryMoveToXYZ(plateToPress.xCoord, plateToPress.yCoord, 
							plateToPress.zCoord, speed);
					System.out.println("Recalculating the path to plate!");
				} else {
					System.out.println("Following the path!");
				}
			} else {
				if(ambushPlace == null) {
					System.out.println("Getting ready to ambush!");
					Vec3d vec3 = null;
					boolean invisible = false;
					do {
						vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 4, 0, plateToPress);
						if(vec3 != null) {
							if (vec3.xCoord >= labLimits[0] && vec3.xCoord <= labLimits[1] &&
								vec3.zCoord >= labLimits[2] && vec3.zCoord <= labLimits[3]) {
								//controlla se va bene che sia visibile il plate o meglio il blocco
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
						System.out.println("Ambush place: " + vec3);
					}
				} else {
					if(entity.getPosition().getX() == MathHelper.floor_double(ambushPlace.xCoord) &&
							entity.getPosition().getZ() == MathHelper.floor_double(ambushPlace.zCoord)) {
						entity.getNavigator().clearPathEntity();
						
						if (lastPosition != null) {
							fakeRotationEntity.setPosition(lastPosition.xCoord, lastPosition.yCoord, lastPosition.zCoord);
							entity.faceEntity(fakeRotationEntity, 360F, 360F);
						}
						resetVariables();
						System.out.println("Reached ambush place!");
					} else if (entity.getNavigator().getPath() == null || (entity.motionX == 0 && entity.motionZ == 0)) {//se Lily non si sta muovendo
						this.entity.getNavigator().tryMoveToXYZ(ambushPlace.xCoord, ambushPlace.yCoord, 
								ambushPlace.zCoord, speed);
						System.out.println("Recalculating the path to ambush place!");
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
