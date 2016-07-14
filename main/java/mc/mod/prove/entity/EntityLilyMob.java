package mc.mod.prove.entity;

import mc.mod.prove.gui.MasterInterfacer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/*
 * Serve per gestire il singolo mob spawnato
 */

public class EntityLilyMob extends EntityPig{

	public EntityLilyMob(World worldIn) {
		super(worldIn);
		initTasks();
	}
	
	private void initTasks(){
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world  = Minecraft.getMinecraft().theWorld;
		

		if(player != null) {
			//this.tasks.addTask(0, new EntityAIFleePlayer(this, player, 2D));
			//this.tasks.addTask(0, new EntityAIHuntPlayer(this, player, 1D));
			//this.tasks.addTask(1, new EntityAIFlee(this, player, 1));
			
			player.openGui(MasterInterfacer.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		}

		
		//this.tasks.addTask(0, new EntityAIBayesianCentral(this, player));
	}

}
