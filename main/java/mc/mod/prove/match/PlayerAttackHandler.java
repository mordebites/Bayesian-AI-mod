package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.EntityLilyMob;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAttackHandler {
	@SubscribeEvent
	public void onAttack(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityLilyMob && MainRegistry.match.isMatchStarted()) {
			System.out.println("Oh mah gawd you hurt may");
		}
	}
}
