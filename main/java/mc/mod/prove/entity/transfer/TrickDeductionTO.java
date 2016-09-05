package mc.mod.prove.entity.transfer;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TrickDeductionTO {
	EntityDistance playerInSight;
	EntityDistance blockSound;
	EntityDistance lightChange; 
	EntityDistance stepSound;
	BlockEvent lastLight;
	BlockEvent lastSound;
	Entity player;
	
	public TrickDeductionTO(EntityDistance playerInSight,
			EntityDistance blockSound, EntityDistance lightChange,
			EntityDistance stepSound, BlockEvent lastLight,
			BlockEvent lastSound, Entity player) {
		
		this.playerInSight = playerInSight;
		this.blockSound = blockSound;
		this.lightChange = lightChange;
		this.stepSound = stepSound;
		this.lastLight = lastLight;
		this.lastSound = lastSound;
		this.player = player;
	}
	
	public EntityDistance getPlayerInSight() {
		return playerInSight;
	}
	public EntityDistance getBlockSound() {
		return blockSound;
	}
	public EntityDistance getLightChange() {
		return lightChange;
	}
	public EntityDistance getStepSound() {
		return stepSound;
	}
	public BlockEvent getLastLight() {
		return lastLight;
	}
	public BlockEvent getLastSound() {
		return lastSound;
	}
	public Entity getPlayer() {
		return player;
	}
	
}
