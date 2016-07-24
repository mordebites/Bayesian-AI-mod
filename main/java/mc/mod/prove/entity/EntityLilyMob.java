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
			//this.tasks.addTask(0, new EntityAISuspect(this, player, 1));
			//this.tasks.addTask(0, new EntityAIHunt(this, player, 1D));
		
			this.tasks.addTask(1, new EntityAILilyCentral(this, player));
		}
	}
	
	public void jump() {
		this.motionY = (double)this.getJumpUpwardsMotion();

	 	System.out.println("Sprinting, baby!");
        this.motionX += 0.3*Math.signum(this.motionX);
        this.motionZ += 0.3*Math.signum(this.motionX);

        this.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}
}
