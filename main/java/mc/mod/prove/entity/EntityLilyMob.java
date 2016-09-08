package mc.mod.prove.entity;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.ai.EntityAILilyProva;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityLilyMob extends EntityVillager {
	private final static int HIT_DISTANCE = 1;
	
	public EntityLilyMob(World worldIn) {
		super(worldIn);
		MainRegistry.lily = this;
		initTasks();
		//InventoryContentHandler.removeLilyEggs(Minecraft.getMinecraft().thePlayer);

	}

	public void initTasks() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if (player != null) {
			this.tasks.addTask(0, new EntityAILilyProva(this, player));

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
	
	@Override
	public void performHurtAnimation(){
		double distance = getDistance();
		if (distance <= HIT_DISTANCE) {
			super.performHurtAnimation();
		}
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		boolean result = super.isEntityInvulnerable(source);
		if (source.isCreativePlayer()){
			double distance = getDistance();
			
			if(distance > HIT_DISTANCE) {
				result = true;
			}
		}
		return result;
	}
	
	private double getDistance() {
		return Minecraft.getMinecraft().thePlayer.getPositionVector().distanceTo(this.getPositionVector());
	}
}
