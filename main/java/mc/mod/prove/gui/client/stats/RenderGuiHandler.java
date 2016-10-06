package mc.mod.prove.gui.client.stats;

import mc.mod.prove.MainRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderGuiHandler {
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		// if experience bar is not on screen
		// then mod's widgets aren't shown either
		if (event.getType() != ElementType.EXPERIENCE)
			return;
		
		if (MainRegistry.match.isRoundStarted()) {
			new OnGameStats(Minecraft.getMinecraft());
		} else {
			new CountDownTimer(Minecraft.getMinecraft());
		}
	}
}