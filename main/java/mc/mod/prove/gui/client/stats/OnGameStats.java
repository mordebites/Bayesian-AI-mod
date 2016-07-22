package mc.mod.prove.gui.client.stats;

import mc.mod.prove.MainRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

// interfaccia che mostra i widgets principali del gioco

public class OnGameStats extends Gui {
	private static final int REDSTR = Integer.parseInt("FF0000", 16);
	private static final int YELLOWSTR = Integer.parseInt("FFAA00", 16);

	private static final ResourceLocation texture = new ResourceLocation(
			"mainregistry", "textures/sight_bar.png");

	private static final int MAXSIGHT = 10;
	private int currentSight = 0;

	private Minecraft mc;
	private ScaledResolution scaled;
	private int width;
	private int height;

	public OnGameStats(Minecraft mc) {
		if (!MainRegistry.match.isRoundStarted())
			return;
		
		this.mc = mc;

		this.scaled = new ScaledResolution(this.mc);
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();

		// TODO implementare i round
		drawRound(1, MainRegistry.match.getMaxRound());

		drawTime();

		// sight suspect bar, prima si setta il current sight value
		// poi si disegna la barra
		drawSightBar();

	}

	private void drawSightBar() {
		// prelevo i valori della vista del mob
		int currentSightValue = MainRegistry.match.getSightValue();
		int maxSightValue = MainRegistry.match.getMaxSightValue();

		int xPos = 10;
		int yPos = 10;
		this.mc.getTextureManager().bindTexture(texture);

		// Add this block of code before you draw the section of your texture
		// containing transparency
		GlStateManager.pushAttrib();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		// alpha test and blend needed due to vanilla or Forge rendering bug
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		// Here we draw the background bar which contains a transparent section;
		// note the new size
		drawTexturedModalRect(xPos, yPos, 0, 0, 56, 9);
		// You can keep drawing without changing anything
		int sightbarwidth = (int) (((float) currentSightValue / maxSightValue) * 49);
		drawTexturedModalRect(xPos + 3, yPos + 3, 0, 9, sightbarwidth, 3);
		String s = "Lily's sight " + currentSightValue + "/" + maxSightValue;
		yPos += 10;
		this.mc.fontRendererObj.drawString(s, xPos + 1, yPos, 0);
		this.mc.fontRendererObj.drawString(s, xPos - 1, yPos, 0);
		this.mc.fontRendererObj.drawString(s, xPos, yPos + 1, 0);
		this.mc.fontRendererObj.drawString(s, xPos, yPos - 1, 0);
		this.mc.fontRendererObj.drawString(s, xPos, yPos, YELLOWSTR);
		GlStateManager.popAttrib();
	}

	private void drawRound(int currentRound, int maxRound) {
		String finalString = currentRound + "/" + maxRound;

		drawCenteredString(mc.fontRendererObj, "Round:", width / 2, 10,
				YELLOWSTR);
		drawCenteredString(mc.fontRendererObj, finalString, width / 2, 20,
				YELLOWSTR);
	}

	private void drawTime() {
		int stringColor = YELLOWSTR;

		// costruisco la stringa del timer con i minuti ed i secondi
		String minuti = "0" + MainRegistry.match.getMinutesTime();
		int secs = MainRegistry.match.getSecsTime();
		String secondi = (secs < 10) ? ("0" + secs) : (secs + "");

		// controllo se la stringa deve essere colorata di rosso
		// per indicare il tempo che sta per scadere
		if ((MainRegistry.match.getMinutesTime() <= 1 && MainRegistry.match.getSecsTime() <= 30)
				|| MainRegistry.match.getMinutesTime() < 1) {
			stringColor = REDSTR;
		}

		if (MainRegistry.match.getMinutesTime() < 0) {
			drawCenteredString(mc.fontRendererObj, "STAHP!", width - 20, 10,
					stringColor);
		} else {
			drawCenteredString(mc.fontRendererObj, minuti + ":" + secondi,
					width - 20, 10, stringColor);
		}
	}
}
