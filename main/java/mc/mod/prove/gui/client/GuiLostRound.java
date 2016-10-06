package mc.mod.prove.gui.client;

import java.io.IOException;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiLostRound extends GuiScreen {
	private GuiButton okay_button;
	private GuiTextField texter;

	public GuiLostRound() {
		MainRegistry.match.setGamePaused(true);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.texter.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void initGui() {
		int centered_width = (this.width / 2);
		int centered_height = (this.height / 2);

		this.texter = new GuiTextField(0, this.fontRendererObj,
				centered_width - 120, centered_height - 60, 240, 20);
		texter.setMaxStringLength(90);
		texter.setText("You lost the round, man!");
		this.texter.setFocused(true);

		String message = "";
		if(MainRegistry.match.getCurrentRound() == MainRegistry.match.getRoundsNumber()) {
			message = "Ok...";
		} else {
			message = "Let me try again!";
		}
		this.buttonList.add(this.okay_button = new GuiButton(0,
				centered_width - 100, centered_height - 24, message));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.okay_button) {
			ModGuiHandler.closeCurrentGui();
			if(MainRegistry.match.getCurrentRound() == MainRegistry.match.getRoundsNumber()) {
				MainRegistry.match.stopMatch();
			}
		}
	}

	@Override
	public void onGuiClosed() {
		// tolgo il gioco dallo stato di pausa
		MainRegistry.match.setGamePaused(false);
	}
}
