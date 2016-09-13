package mc.mod.prove.entity.ai.basic;

import java.util.Iterator;
import java.util.Random;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.movement.JumpHelper;
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
	private EntityLiving fakeRotationEntity = new EntityCow(
			Minecraft.getMinecraft().theWorld);

	// vectors
	private Vec3d plateToPress = null;
	private Vec3d ambushPlace = null;

	// position flags
	private boolean platePressed = false; // shows if the plate chosen to trick
											// was pressed
	private boolean movingToPlate = false;
	private boolean ambushPlaceChosen = false;

	private int stopTimer = 0;

	public EntityAITrick(EntityCreature entity, double speed) {
		this.entity = entity;
		this.speed = speed;
	}

	public void setIterators(Iterator lights, Iterator sounds) {
		int no = rdm.nextInt(2) + 1;
		if (no == 1) {
			iters[0] = lights;
			iters[1] = sounds;
		} else {
			iters[0] = sounds;
			iters[1] = lights;
		}
	}

	public void setPlayerLastPosition(BlockPos lastPosition) {
		// se non � nota l'ultima posizione
		if (lastPosition != null) {
			this.lastPosition = new Vec3d(lastPosition.getX(),
					lastPosition.getY(), lastPosition.getZ());
		} else if (plateToPress == null) {
			int pos = rdm.nextInt(4);
			int i = 0;
			while (plateToPress == null) {
				if (iters[0].hasNext()) {
					if (i == pos) {
						BlockPos b = (BlockPos) iters[0].next();
						plateToPress = new Vec3d(b.getX(), b.getY(), b.getZ());
						System.out.println("Plate chosen without last position!");
					}
				} else if (iters[1].hasNext()) {
					if (i == pos) {
						BlockPos b = (BlockPos) iters[1].next();
						plateToPress = new Vec3d(b.getX(), b.getY(), b.getZ());
						System.out.println("Plate chosen without last position!");
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
	public void startExecuting() {
		// se non ha scelto il blocco da premere
		if (plateToPress == null) {
			BlockPos a = null;
			int i = 0;

			while (plateToPress == null && i < 2) {
				Iterator itr = iters[i];
				while (itr.hasNext()) {
					a = ((BlockPos) itr.next());
					Vec3d actual = new Vec3d(a.getX(), a.getY(), a.getZ());
					if (actual.distanceTo(lastPosition) >= 10) {
						plateToPress = actual;
						System.out.println("Plate chosen!");
					}
				}
				i++;
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		if (!movingToPlate) {
			if (plateToPress == null) {
				this.setPlayerLastPosition(null);
			}
			if (this.entity.getNavigator().tryMoveToXYZ(plateToPress.xCoord,
					plateToPress.yCoord, plateToPress.zCoord, speed)) {
				movingToPlate = true;
				System.out.println("Moving to plate!");
			} else {
				//se non pu� raggiungere la piattaforma scelta in precedenza
				plateToPress = null;
				System.out.println("Plate to press was reset!");
			}
		} else {
			if (!platePressed) {
				if (entity.getPosition().getX() == MathHelper
						.floor_double(plateToPress.xCoord)
						&& entity.getPosition().getZ() == MathHelper
								.floor_double(plateToPress.zCoord)) {
					platePressed = true;
					System.out.println("Plate pressed!");
				} else if (entity.getNavigator().getPath() == null
						|| (entity.motionX == 0 && entity.motionZ == 0)) {
					this.entity.getNavigator().tryMoveToXYZ(
							plateToPress.xCoord, plateToPress.yCoord,
							plateToPress.zCoord, speed);
				}
			} else {
				if (ambushPlace == null) {
					Vec3d vec3 = null;
					boolean invisible = false;
					do {
						vec3 = RandomPositionGenerator
								.findRandomTargetBlockAwayFrom(entity, 4, 0,
										plateToPress);
						if (vec3 != null) {
							if (vec3.xCoord >= MainRegistry.MIN_X_LAB
									&& vec3.xCoord <= MainRegistry.MAX_X_LAB
									&& vec3.zCoord >= MainRegistry.MIN_Z_LAB
									&& vec3.zCoord <= MainRegistry.MAX_Z_LAB) {
								// controlla se va bene che sia visibile il
								// plate o meglio il blocco
								if (entity.worldObj.rayTraceBlocks(
										plateToPress, vec3, false, true, false) != null) {
									invisible = true;
								}
							}
						}
					} while (vec3 == null || !invisible);

					double newPosX = vec3.xCoord;
					double newPosY = vec3.yCoord;
					double newPosZ = vec3.zCoord;

					if (this.entity.getNavigator().tryMoveToXYZ(newPosX,
							newPosY, newPosZ, speed)) {
						ambushPlace = vec3;
						System.out.println("Ambush place set! Moving to ambush place!");
					}
				} else {
					if (entity.getPosition().getX() == MathHelper
							.floor_double(ambushPlace.xCoord)
							&& entity.getPosition().getZ() == MathHelper
									.floor_double(ambushPlace.zCoord)) {
						entity.getNavigator().clearPathEntity();

						if (lastPosition != null) {
							fakeRotationEntity.setPosition(lastPosition.xCoord,
									lastPosition.yCoord, lastPosition.zCoord);
							entity.faceEntity(fakeRotationEntity, 360F, 360F);
						}
						resetVariables();
					} else if (entity.getNavigator().getPath() == null
							|| (entity.motionX == 0 && entity.motionZ == 0)) {// se
																				// Lily
																				// non
																				// si
																				// sta
																				// muovendo
						this.entity.getNavigator().tryMoveToXYZ(
								ambushPlace.xCoord, ambushPlace.yCoord,
								ambushPlace.zCoord, speed);
						System.out.println("Moving to ambush place!");
					}
				}
			}
		}
		// JumpHelper.pathHelper(entity);
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
	
	
	public boolean isTricking() {
		return tricking;
	}

	public void setTricking() {
		this.tricking = true;
	}
	
	public void setNotTricking() {
		this.tricking = false;
		resetVariables();
	}
}
