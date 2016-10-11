package mc.mod.lilysrc.eventhandler;

import mc.mod.lilysrc.MainRegistry;
import mc.mod.lilysrc.match.InventoryContentHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/**
 * Handles the loading of the player from file
 */
public class PlayerLogHandler {
	
	@SubscribeEvent
	public void onLoadPlayer(PlayerEvent.LoadFromFile event) {
		System.out.println("Player loading from file...");
		if (MainRegistry.lily != null) {
			MainRegistry.lily.setDead();
		}
		//adds Lily's eggs to player's inventory
		InventoryContentHandler.insertLilyEggs(event.getEntityPlayer());
		event.getEntityPlayer().setSpawnPoint(MainRegistry.LAB_PLATE, true);
	}
}
