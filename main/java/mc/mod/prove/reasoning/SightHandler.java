package mc.mod.prove.reasoning;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SightHandler {
	
	private enum RelativePosition {
		LEFT, RIGHT, FRONT
	}

	private EntityLivingBase entity;
	private EntityPlayer player;

	public SightHandler(EntityLivingBase entity, EntityPlayer player) {
		this.entity = entity;
		this.player = player;
	}

	//  determines whether the player is visible and how far he is
	public EntityDistance checkPlayerInSight(Entity player,
			int distanceThreshold, boolean playerAlreadySeen) {
		EntityDistance playerInSight = EntityDistance.None;

		// controlla se Lily puo' vedere il giocatore verificando che non ci
		// siano blocchi interposti tra i due
		if (this.isInFieldOfVision(player, (EntityLiving)entity)) {

			// controlla che il giocatore non sia invisibile a causa
			// dell'oscurita'
			if (((player.getBrightness(0) > 0.2 || (player.getBrightness(0) > 0.1 && (player.motionX != 0
					|| player.motionY != 0 || player.motionZ != 0))))
					|| playerAlreadySeen) {

				if (entity.getPositionVector().distanceTo(
						player.getPositionVector()) > distanceThreshold) {
					playerInSight = EntityDistance.Far;
				} else {
					playerInSight = EntityDistance.Close;
				}
			}
		}
		return playerInSight;
	}

	private boolean isInFieldOfVision(Entity observed,
			EntityLiving observer) {
		// save Entity 2's original rotation variables
		float rotationYawPrime = observer.rotationYaw;
		float rotationPitchPrime = observer.rotationPitch;
		// make Entity 2 directly face Entity 1
		observer.faceEntity(observed, 360F, 360F);
		// switch values of prime rotation variables with current rotation
		// variables
		float f = observer.rotationYaw;
		float f2 = observer.rotationPitch;
		observer.rotationYaw = rotationYawPrime;
		observer.rotationPitch = rotationPitchPrime;
		rotationYawPrime = f;
		rotationPitchPrime = f2;
		// assuming field of vision consists of everything within X degrees from
		// rotationYaw and Y degrees from rotationPitch, check if entity 2's
		// current rotationYaw and rotationPitch within this X and Y range
		float X = 80F;
		float Y = 60F;
		float yawFOVMin = observer.rotationYaw - X;
		float yawFOVMax = observer.rotationYaw + X;
		float pitchFOVMin = observer.rotationPitch - Y;
		float pitchFOVMax = observer.rotationPitch + Y;
		boolean flag1 = (yawFOVMin < 0F && (rotationYawPrime >= yawFOVMin + 360F || rotationYawPrime <= yawFOVMax))
				|| (yawFOVMax >= 360F && (rotationYawPrime <= yawFOVMax - 360F || rotationYawPrime >= yawFOVMin))
				|| (yawFOVMax < 360F && yawFOVMin >= 0F
						&& rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin);
		boolean flag2 = (pitchFOVMin <= -180F && (rotationPitchPrime >= pitchFOVMin + 360F || rotationPitchPrime <= pitchFOVMax))
				|| (pitchFOVMax > 180F && (rotationPitchPrime <= pitchFOVMax - 360F || rotationPitchPrime >= pitchFOVMin))
				|| (pitchFOVMax < 180F && pitchFOVMin >= -180F
						&& rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin);
		if (flag1 && flag2 && observer.canEntityBeSeen(observed))
			return true;
		else
			return false;
	}

	// checks whether the entity can see the last activated light
	public EntityDistance checkLight(BlockEvent lastLight, int distanceThreshold) {
		EntityDistance light = EntityDistance.None;

		// if a light was activated
		if (lastLight != null && lastLight.getTimer() > 0) {
			int lightX = lastLight.getPos().getX();
			int lightZ = lastLight.getPos().getZ();

			boolean lightSeen = false;
			EnumFacing facing = entity.getHorizontalFacing();

			// checks if the entity is facing the same direction as the light
			if ((facing == EnumFacing.WEST && (int) entity.posX >= lightX)
					|| (facing == EnumFacing.EAST && (int) entity.posX <= lightX)
					|| (facing == EnumFacing.NORTH && (int) entity.posZ >= lightZ)
					|| (facing == EnumFacing.SOUTH && (int) entity.posZ <= lightZ)) {

				//if the entity can see the light block
				if (entity.worldObj.rayTraceBlocks(new Vec3d(entity.posX,
						entity.posY + (double) entity.getEyeHeight(),
						entity.posZ), new Vec3d(lightX, 4, lightZ), false,
						true, false) == null) {
					lightSeen = true;
				} else {
					RelativePosition pos;
					switch (facing) {
					case EAST: {
						pos = getRelativePosition(lightZ, facing);
						lightSeen = handleCardinal(lightZ, lightX, facing, pos);
						break;
					}
					case WEST: {
						pos = getRelativePosition(lightZ, facing);
						lightSeen = handleCardinal(lightZ, lightX, facing, pos);
						break;
					}
					case SOUTH: {
						pos = getRelativePosition(lightX, facing);
						lightSeen = handleCardinal(lightX, lightZ, facing, pos);
						break;
					}
					case NORTH: {
						pos = getRelativePosition(lightX, facing);
						lightSeen = handleCardinal(lightX, lightZ, facing, pos);
						break;
					}
					default: {
						System.err.println("Lily is facing an invalid direction!");
					}
					}
				}
			}

			if (lightSeen) {
				System.out.println("Lily saw a light!");
				if (entity.getPositionVector().distanceTo(
						new Vec3d(lastLight.getPos().getX(), lastLight.getPos()
								.getY(), lastLight.getPos().getZ())) > distanceThreshold) {
					light = EntityDistance.Far;
				} else {
					light = EntityDistance.Close;
				}
			}
		}
		System.out.println(light);
		return light;
	}
	//determines if the light block is on the left, on the right or in front of the entity
	private RelativePosition getRelativePosition(int light, EnumFacing facing) {
		RelativePosition pos = null;
		
		switch (facing) {
		case EAST: {
			if (light < (int) entity.posZ) {
				pos = RelativePosition.LEFT;
			} else if (light > (int) entity.posZ) {
				pos = RelativePosition.RIGHT;
			} else {
				pos = RelativePosition.FRONT;
			}
			break;
		}
		case WEST: {
			if (light > (int) entity.posZ) {
				pos = RelativePosition.LEFT;
			} else if (light < (int) entity.posZ) {
				pos = RelativePosition.RIGHT;
			} else {
				pos = RelativePosition.FRONT;
			}
			break;
		}
		case SOUTH: {
			if (light > (int) entity.posX) {
				pos = RelativePosition.LEFT;
			} else if (light < (int) entity.posX) {
				pos = RelativePosition.RIGHT;
			} else {
				pos = RelativePosition.FRONT;
			}
		
			break;
		}
		case NORTH: {
			if (light < (int) entity.posX) {
				pos = RelativePosition.LEFT;
			} else if (light > (int) entity.posX) {
				pos = RelativePosition.RIGHT;
			} else {
				pos = RelativePosition.FRONT;
			}
			break;
		}
		default: {
			System.err.println("Lily is facing an invalid direction!");
		}
		}
		return pos;
	}
	
	//tells the cross(...) method the sides of the light block to check.
	//It's used to check whether the entity can see  the light even though
	//it can't see the block.
	private boolean handleCardinal(int lightMainVar, int lightSecVar, EnumFacing facing, RelativePosition pos) {
		boolean lightSeen = false;
		
		String axis1 = "NS";
		String axis2 = "WE";
		if(facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
			axis1 = "WE";
			axis2 = "NS";
		}
		
		if(pos != RelativePosition.FRONT) {
			if ((pos == RelativePosition.LEFT && (facing == EnumFacing.NORTH && facing == EnumFacing.EAST))
				||(pos == RelativePosition.RIGHT && (facing == EnumFacing.SOUTH && facing == EnumFacing.WEST))) {
				lightSeen = this.cross(lightMainVar + 1, lightMainVar + 4, lightSecVar, axis1, true);
			} else {
				lightSeen = this.cross(lightMainVar - 1, lightMainVar - 4, lightSecVar, axis1, false);
			}
			
			if (!lightSeen) {
				lightSeen = this.cross(lightSecVar + 1, lightSecVar + 4, lightMainVar, axis2, true);
				if(!lightSeen) {
					lightSeen = this.cross(lightSecVar - 1, lightSecVar - 4, lightMainVar, axis2, false);
				}
			}
		} else {
			lightSeen = this.cross(lightMainVar + 1, lightMainVar + 4, lightSecVar, axis1, true);
			if(!lightSeen) {
				lightSeen = this.cross(lightMainVar - 1, lightMainVar - 4, lightSecVar, axis1, false);
				if(!lightSeen) {
					if (facing == EnumFacing.NORTH || facing == EnumFacing.WEST) {
						lightSeen = this.cross(lightSecVar + 1, lightSecVar + 4, lightMainVar, axis2, true);
					} else {
						lightSeen = this.cross(lightSecVar - 1, lightSecVar - 4, lightMainVar, axis2, false);
					}
				}
			}
		}
		return lightSeen;	
	}

	//checks three sides of the light block and surrounding blocks to know if
	//the illuminated floor blocks are seen
	private boolean cross(int start, int end, int otherCoord, String axis,
			boolean increment) {
		boolean visible = false;
		int i = start;
		BlockPos pos = null;

		if (axis.compareTo("NS") == 0) {
			pos = new BlockPos(i, 4, otherCoord);
		} else {
			pos = new BlockPos(otherCoord, 4, i);
		}

		IBlockState blockState = Minecraft.getMinecraft().theWorld
				.getBlockState(pos);

		while (!visible && !(blockState.getBlock() instanceof BlockStone)
				&& i >= (end)) {
			Vec3d blockToCheck = null;
			if (axis.compareTo("NS") == 0) {
				blockToCheck = new Vec3d(i, 4, otherCoord);
			} else {
				blockToCheck = new Vec3d(otherCoord, 4, i);
			}
			if (entity.worldObj.rayTraceBlocks(new Vec3d(entity.posX,
					entity.posY + (double) entity.getEyeHeight(), entity.posZ),
					blockToCheck, false, true, false) == null) {
				visible = true;
			}

			i = increment ? i + 1 : i - 1;
			if (axis.compareTo("NS") == 0) {
				pos = new BlockPos(i, 4, otherCoord);
			} else {
				pos = new BlockPos(otherCoord, 4, i);
			}
			blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		}
		return visible;
	}
}
