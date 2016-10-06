package mc.mod.prove.entity.ai;

import java.util.HashMap;

import mc.mod.prove.entity.ai.basic.EntityAIFlee;
import mc.mod.prove.entity.ai.basic.EntityAIHunt;
import mc.mod.prove.entity.ai.basic.EntityAILookAround;
import mc.mod.prove.entity.ai.basic.EntityAISuspect;
import mc.mod.prove.entity.ai.basic.EntityAITrick;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The factory to abstract the instantiation of the current basic AI.
 */
public class EntityAIFactory {
	private HashMap<String, EntityAIBase> AIs = new HashMap<String, EntityAIBase>();
	private EntityAILilyCentral central;
	
	public EntityAIFactory(EntityAILilyCentral central){
		this.central = central;
		EntityPlayer player = (EntityPlayer)central.getPlayer();
		EntityCreature entity = (EntityCreature) central.getEntity();

		AIs.put("LookAround", new EntityAILookAround(entity, 0.4));
		AIs.put("Hunt", new EntityAIHunt(entity, player, 0.65));
		AIs.put("Flee", new EntityAIFlee(entity, player, 0.75));		
		
		EntityAISuspect su = new EntityAISuspect(entity, player, new Double(0.55));
		su.setPlayerLastPosition(central.getLastPlayerPosition());
		AIs.put("Suspect", su);
		
		EntityAITrick tr = new EntityAITrick(entity, new Double(0.6));
		tr.setIterators(central.getLightPlates(), central.getSoundPlates());
		tr.setPlayerLastPosition(central.getLastPlayerPosition());
		tr.setTricking();
		AIs.put("Trick", tr);
	}
	
	public EntityAIBase getEntityAI(String stateName){
		EntityAIBase ai = null;
		try {
			ai = AIs.get(stateName);
			if(stateName.compareTo("Suspect") == 0) {
				((EntityAISuspect) ai).setPlayerLastPosition(central.getLastPlayerPosition());
			} else if (stateName.compareTo("Trick") == 0) {
				if (!((EntityAITrick) ai).isTricking()) {
					((EntityAITrick) ai).setPlayerLastPosition(central.getLastPlayerPosition());
					((EntityAITrick) ai).setIterators(central.getLightPlates(), central.getSoundPlates());
					((EntityAITrick) ai).setTricking();
				}
			}
		} catch (Exception e){
			System.err.println("Problem occured in the AI factory!");
			System.err.println(e.getMessage());
		}
		return ai;
	}
}