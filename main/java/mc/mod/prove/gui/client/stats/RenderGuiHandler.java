package mc.mod.prove.gui.client.stats;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderGuiHandler {
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		// se la barra dell'esperienza non e' mostrata
		// allora non mostro neanche i widgets di della mod
		if (event.getType() != ElementType.EXPERIENCE)
			return;
		
		new OnGameStats(Minecraft.getMinecraft());
	}
}