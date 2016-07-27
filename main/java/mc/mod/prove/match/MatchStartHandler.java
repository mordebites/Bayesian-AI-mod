package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MatchStartHandler {

	@SubscribeEvent
	public void onEvent(RightClickEmpty event) {
		if (MainRegistry.lily != null) {
			if (AwardHandler.hasItem(Minecraft.getMinecraft().thePlayer, AwardHandler.EMERALDS_TO_PLAY)) {
				ModGuiHandler.createGui(ModGuiHandler.GUI_START_BET);
			} else {
				ModGuiHandler.createGui(ModGuiHandler.GUI_NOT_ENOUGH_MONEY);
			}
		}
	}
}
