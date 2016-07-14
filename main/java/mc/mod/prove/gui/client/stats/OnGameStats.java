package mc.mod.prove.gui.client.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class OnGameStats extends Gui {
	public OnGameStats(Minecraft mc) {
		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
	
		drawCenteredString(mc.fontRendererObj, "yo man", width / 2, (height / 2) - 4, Integer.parseInt("FFAA00", 16));
	}
	
}
