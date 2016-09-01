package mc.mod.prove.eventhandler;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.match.InventoryContentHandler;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerLogHandler {

	@SubscribeEvent
	public void onLoadPlayer(PlayerEvent.LoadFromFile event) {
		//TODO check me
		System.out.println("Player loading from file...");
		if (MainRegistry.lily != null) {
			MainRegistry.lily.setDead();
		}
		InventoryContentHandler.insertLilyEggs(event.getEntityPlayer());
	}

	@SubscribeEvent
	public void onSavePlayerEvent(PlayerEvent.SaveToFile event) {
		System.out.println("Player saving to file..."); 
	}
}
