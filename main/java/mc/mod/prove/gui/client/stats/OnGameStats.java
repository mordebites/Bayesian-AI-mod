package mc.mod.prove.gui.client.stats;

import mc.mod.prove.gui.MasterInterfacer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class OnGameStats extends Gui {
	private Minecraft mc;
	private ScaledResolution scaled;
	private int width;
	private int height;

	public OnGameStats(Minecraft mc) {
		this.mc = mc;

		// incremento il valore dei tick in questo modo posso
		// trovarmi i secondi passati giocando
		
		if (MasterInterfacer.ticks % 20 == 0) {
			MasterInterfacer.secs += 1;
		}
		
		MasterInterfacer.ticks += 1;

		this.scaled = new ScaledResolution(this.mc);
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();

		drawRound(1, 2);
		drawTime();
		drawSuspectBar(3);
	}

	private void drawRound(int currentRound, int maxRound) {
		String finalString = currentRound + "/" + maxRound;
		
		drawCenteredString(mc.fontRendererObj, "Round:", 20, 10,
				Integer.parseInt("FFAA00", 16));
		drawCenteredString(mc.fontRendererObj, finalString, 20, 20,
				Integer.parseInt("FFAA00", 16));
	}

	private void drawTime() {
		drawCenteredString(mc.fontRendererObj, MasterInterfacer.secs + "",
				width - 20, 10, Integer.parseInt("FFAA00", 16));
	}

	private void drawSuspectBar(int percentage) {
		// ----------
		
		String finalString = "";
		
		if (percentage <= 10) {
			for (int i = 0; i < percentage; i++) {
				finalString += "|";
			}
			
			for (int i = 0; i < 10 - percentage; i++) {
				finalString += "-";
			}
		}
		
		drawCenteredString(mc.fontRendererObj, finalString, width / 2, 10,
				Integer.parseInt("FFAA00", 16));
	}

}
