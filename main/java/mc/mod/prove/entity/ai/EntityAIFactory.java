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

public class EntityAIFactory {
	private HashMap<String, EntityAIBase> AIs = new HashMap<String, EntityAIBase>();
	private EntityAILilyCentral central;
	
	public EntityAIFactory(EntityAILilyCentral central){
		this.central = central;
		EntityPlayer player = central.getPlayer();
		EntityCreature entity = central.getEntity();

		AIs.put("LookAround", new EntityAILookAround(entity, 0.8));
		AIs.put("Hunt", new EntityAIHunt(entity, player, 1));
		AIs.put("Flee", new EntityAIFlee(entity, player, 1));		
		
		EntityAISuspect su = new EntityAISuspect(entity, player, new Double(1));
		su.setPlayerLastPosition(central.getLastPlayerPosition());
		AIs.put("Suspect", su);
		
		EntityAITrick tr = new EntityAITrick(entity, new Double(0.8), central.getLabyrinthLimits());
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
			System.err.println("Panic at the Factory!");
			System.err.println(e.getMessage());
		}
		return ai;
	}
}
