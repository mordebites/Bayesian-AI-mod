package mc.mod.prove.gui.client.stats;

import mc.mod.prove.MainRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Class that handles the countdown timer at the beginning of each round.
 */
public class CountDownTimer extends Gui {
	private static final int REDSTR = Integer.parseInt("FF0000", 16);
	private static final int YELLOWSTR = Integer.parseInt("FFAA00", 16);
	private static final int GREENSTR = Integer.parseInt("00FF00", 16);

	private Minecraft mc;
	private ScaledResolution scaled;
	private int width;
	private int height;

	public CountDownTimer(Minecraft mc) {
		if (!MainRegistry.match.isMatchStarted())
			return;

		this.mc = mc;

		this.scaled = new ScaledResolution(this.mc);
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();

		drawCountDown();
	}

	private void drawCountDown() {
		int stringColor = GREENSTR;
		int countDown = MainRegistry.match.getCountDownTime();

		switch (countDown) {
		case 3:
			stringColor = GREENSTR;
			break;
		case 2:
			stringColor = YELLOWSTR;
			break;
		case 1:
			stringColor = REDSTR;
			break;
		}

		drawCenteredString(mc.fontRendererObj, countDown + "", width / 2,
				(height / 2) - 20, stringColor);
	}
}
