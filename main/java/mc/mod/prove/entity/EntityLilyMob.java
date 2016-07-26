package mc.mod.prove.entity;

import mc.mod.prove.entity.ai.EntityAILilyCentral;
import mc.mod.prove.gui.ModGuiHandler;
import mc.mod.prove.match.AwardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityLilyMob extends EntityPig {

	public EntityLilyMob(World worldIn) {
		super(worldIn);
		initTasks();
	}

	private void initTasks() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if (player != null) {
			// appena spawnato l'npc controllo che il player abbia abbastanza
			// emeralds per giocare la partita
			
			if (AwardHandler.hasItem(player, AwardHandler.EMERALDS_TO_PLAY)) {
				ModGuiHandler.createGui(ModGuiHandler.GUI_START_BET);
			} else {
				ModGuiHandler.createGui(ModGuiHandler.GUI_NOT_ENOUGH_MONEY);
			}

			this.tasks.addTask(0, new EntityAILilyCentral(this, player));
		}
	}

	public void jump() {
		this.motionY = (double) this.getJumpUpwardsMotion();

		System.out.println("Sprinting, baby!");
		this.motionX += 0.4 * Math.signum(this.motionX);
		this.motionZ += 0.4 * Math.signum(this.motionX);

		this.isAirBorne = true;
		net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}

	protected SoundEvent getAmbientSound() {
		return null;
	}
}
