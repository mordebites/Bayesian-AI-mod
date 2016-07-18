package mc.mod.prove.gui.client.stats;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import mc.mod.prove.gui.MasterInterfacer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

// interfaccia che mostra i widgets principali del gioco

public class OnGameStats extends Gui {
	private Minecraft mc;
	private ScaledResolution scaled;
	private int width;
	private int height;

	public OnGameStats(Minecraft mc) {
		this.mc = mc;

		this.scaled = new ScaledResolution(this.mc);
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();

		drawRound(1, 2);
		drawTime(MasterInterfacer.generalTime);
		drawSuspectBar(MasterInterfacer.suspectValue);
	}

	private void drawRound(int currentRound, int maxRound) {
		String finalString = currentRound + "/" + maxRound;
		
		drawCenteredString(mc.fontRendererObj, "Round:", 20, 10,
				Integer.parseInt("FFAA00", 16));
		drawCenteredString(mc.fontRendererObj, finalString, 20, 20,
				Integer.parseInt("FFAA00", 16));
	}

	private void drawTime(long time) {
		drawCenteredString(mc.fontRendererObj, time + "",
				width - 20, 10, Integer.parseInt("FFAA00", 16));
	}

	private void drawSuspectBar(int value) {
		
		int max_lines = 10;
		
		if (value < 0 || value > max_lines) {
			return;
		}
		
		String finalString = "";
		
		for (int i = 1; i <= max_lines; i++) {
			if (i <= value) {
				finalString += "|";
			} else {
				finalString += "-";
			}
		}
		
		drawCenteredString(mc.fontRendererObj, finalString, width / 2, 10,
				Integer.parseInt("FFAA00", 16));
	}

}
