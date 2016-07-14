package mc.mod.prove;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class AttackHandler {
	@SubscribeEvent
	public void onAttackEntity(PlayerInteractEvent event) {
		//System.out.println(event.getWorld().getSeed());
		System.out.println("yo loosed!");
	}
}
