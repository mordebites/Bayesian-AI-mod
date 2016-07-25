package mc.mod.prove.reasoning;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SightHandler {
	private EntityLivingBase entity;
	
	public SightHandler(EntityLivingBase entity){
		this.entity = entity;
	}
	
	//stabilisce se il giocatore � visibile e a quale distanza
	public EntityDistance checkPlayerInSight(Entity player, int distanceThreshold){
		EntityDistance playerInSight = EntityDistance.None;
		
		if(entity.canEntityBeSeen(player)) {
			if(entity.getPositionVector().distanceTo(player.getPositionVector()) > distanceThreshold) {
				playerInSight = EntityDistance.Far;
			} else {
				playerInSight = EntityDistance.Close;
			}
		}
		
		return playerInSight;
	}
	
	//controlla se Lily pu� vedere l'ultima luce che si � accesa
	public EntityDistance checkLight(BlockEvent lastLight, int distanceThreshold){
		EntityDistance light = EntityDistance.None;
		
		//se � stata accesa qualche luce
		if(lastLight != null && lastLight.getTimer() > 0) {
			int lightX = lastLight.getPos().getX();
			int lightZ = lastLight.getPos().getZ();
			
			boolean lightSeen = false;
			EnumFacing facing = entity.getHorizontalFacing();
			System.out.println("Lily is facing " + facing.toString());
			
			//controlla se l'NPC sta guardando nella stessa direzione della luce
			if ((facing == EnumFacing.WEST && (int) entity.posX >= lightX)
				|| (facing == EnumFacing.EAST && (int) entity.posX <= lightX)
				|| (facing == EnumFacing.NORTH && (int) entity.posZ >= lightZ )
				|| (facing == EnumFacing.SOUTH && (int) entity.posZ <= lightZ)) {
				
				if(entity.worldObj.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), new Vec3d(lightX, 4, lightZ), false, true, false) == null) {
					lightSeen = true;
				} else {
					switch(facing) {
						case EAST : {
							lightSeen = handleEast(lightX, lightZ);
							break;
						}
						case WEST : {
							lightSeen = handleWest(lightX, lightZ);
							break;
						}
						case SOUTH : {
							lightSeen = handleSouth(lightX, lightZ);
							break;
						}
						case NORTH : {
							lightSeen = handleNorth(lightX, lightZ);
							break;
						}
						default: {
							System.err.println("Lily is facing an invalid direction!");
						}
					}
				}
			}
			
						
			if (lightSeen) {
				lastLight.setPerceived(true);
				if(entity.getPositionVector().distanceTo(new Vec3d(lastLight.getPos().getX(), lastLight.getPos().getY(), lastLight.getPos().getZ())) > distanceThreshold) {
					light = EntityDistance.Far;
				} else {
					light = EntityDistance.Close;
				}
			} 	
		}
		return light;
	}
	
	private boolean handleNorth(int lightX, int lightZ) {
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightX < (int) entity.posX) {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightX > (int) entity.posX) {
			lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}
	
	private boolean handleSouth(int lightX, int lightZ) {
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightX > (int) entity.posX) {
			lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightX < (int) entity.posX) {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}

	private boolean handleWest(int lightX, int lightZ) {
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightZ > (int) entity.posZ) {
			lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightZ < (int) entity.posZ) {
			lightSeen = this.cross(lightZ+1, lightZ-4, lightX, "WE", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}

	private boolean handleEast(int lightX, int lightZ){
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightZ < (int) entity.posZ) {
			lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightZ > (int) entity.posZ) {
			lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}
	
	private boolean cross(int start, int end, int otherCoord, String axis, boolean increment) {
		boolean visible = false;
		int i = start;
		BlockPos pos = null;
		
		if(axis.compareTo("NS") == 0) {
			pos = new BlockPos(i, 4, otherCoord);
		} else {
			pos = new BlockPos(otherCoord, 4, i);
		}
		
		IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		
		while (!visible && !(blockState.getBlock() instanceof BlockStone) && i >= (end)){
			Vec3d blockToCheck = null;
			if(axis.compareTo("NS") == 0) {
				blockToCheck = new Vec3d(i, 4, otherCoord);
			} else {
				blockToCheck = new Vec3d(otherCoord, 4, i);
			}
			if(entity.worldObj.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), blockToCheck, false, true, false) == null) {
				visible = true;
			}
			
			i = increment? i+1 : i-1;
			if(axis.compareTo("NS") == 0) {
				pos = new BlockPos(i, 4, otherCoord);
			} else {
				pos = new BlockPos(otherCoord, 4, i);
			}
			blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		}
		return visible;
	}
	
}
