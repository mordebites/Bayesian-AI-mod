package mc.mod.prove.entity;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.ai.EntityAILilyCentral;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Class representing the main entity of the mod, Lily. Handles AI and health.
 */
public class EntityLilyMob extends EntityVillager {
	//the player needs to be this close to Lily to hit her
	private final static int HIT_DISTANCE = 1;
	
	public EntityLilyMob(World worldIn) {
		super(worldIn);
		MainRegistry.lily = this;
		initTasks();

	}

	public void initTasks() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if (player != null) {
			//adds the AI that handles basic behaviours
			this.tasks.addTask(0, new EntityAILilyCentral(this, player));

			System.out.println("AI set!");
		}
	}
	
	//prevents Lily from making the usual villager sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}
	
	//forces the match to end when Lily dies
	@Override
	public void setDead() {
		super.setDead();
		MainRegistry.lily = null;
		
		if(MainRegistry.match.isMatchStarted()) {
			MainRegistry.match.stopMatch();
		}
		
		System.out.println("Oh no, Lily died!");
	}
	
	//the animation for the damage is performed
	//only if the player's distance is below HIT_DISTANCE
	@Override
	public void performHurtAnimation(){
		double distance = getDistance();
		if (distance <= HIT_DISTANCE) {
			super.performHurtAnimation();
		}
	}
	
	//Lily gets damaged
	//only if the player's distance is below HIT_DISTANCE
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
