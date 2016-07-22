package mc.mod.prove.entity;

import mc.mod.prove.entity.ai.EntityAILilyCentral;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityLilyMob extends EntityPig{

	public EntityLilyMob(World worldIn) {
		super(worldIn);
		initTasks(); 
	}
	
	private void initTasks(){
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if(player != null) {
			//this.tasks.addTask(0, new EntityAIFlee(this, player, 2D));
			//this.tasks.addTask(0, new EntityAIHunt(this, player, 1D));
			
			//TODO togli commento
			//this.tasks.addTask(1, new EntityAILilyCentral(this, player));
		}
	}

}
