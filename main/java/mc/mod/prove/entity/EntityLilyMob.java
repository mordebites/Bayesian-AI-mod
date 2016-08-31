package mc.mod.prove.entity;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.ai.EntityAILilyCentral;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class EntityLilyMob extends EntityVillager {
	public EntityLilyMob(World worldIn) {
		super(worldIn);
		MainRegistry.lily = this;
		initTasks();
		//InventoryContentHandler.removeLilyEggs(Minecraft.getMinecraft().thePlayer);

	}

	public void initTasks() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if (player != null) {
			this.tasks.addTask(0, new EntityAILilyCentral(this, player));
			this.tasks.addTask(0, new EntityAIWander(this, 1));

			System.out.println("Lily's AI set!");
		}
	}

	
	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}
	
	
	@Override
	public void setDead() {
		super.setDead();
		MainRegistry.lily = null;
		
		if(MainRegistry.match.isMatchStarted()) {
			MainRegistry.match.stopMatch();
		}
		
		System.out.println("Oh noes Lily died!");
	}
}
