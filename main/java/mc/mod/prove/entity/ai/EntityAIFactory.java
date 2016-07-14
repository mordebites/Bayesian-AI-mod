package mc.mod.prove.entity.ai;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class EntityAIFactory {
	private HashMap<String, Object[][]> AIs = new HashMap<String, Object[][]>();
	private EntityPlayer player;
	private EntityCreature entity;
	
	public EntityAIFactory(EntityPlayer player, EntityCreature entity){
		this.player = player;
		this.entity = entity;
		
		Object[][] la = {{EntityCreature.class, Double.class}, {entity, 1}};
		AIs.put("LookAround", la);
		
		Object[][] huntNflee = {{EntityCreature.class, EntityPlayer.class, Double.class},
				{entity, player, 1.5}};
		AIs.put("Hunt", huntNflee);
		AIs.put("Flee", huntNflee);
		
		
		Object[] temp = new Object[4];
		temp[0] = entity;
		temp[1] = player;
		temp[2] = new Double(1.5);
		Object[][] suspect = {{EntityCreature.class, EntityPlayer.class, Double.class, 
			BlockPos.class}, temp};
		AIs.put("Suspect", suspect);
	}
	
	public EntityAIBase getEntityAI(String stateName){
		EntityAIBase ai = null;
		try {
			Class AIclass = Class.forName("EntityAI" + stateName);
			Object[] param = AIs.get(stateName);
			Constructor constr = AIclass.getConstructor((Class[])param[0]);
			constr.newInstance((Object[]) param[1]);
		} catch (Exception e){
			System.err.println("Panic at the Factory!");
		}
		return ai;
	}
}
