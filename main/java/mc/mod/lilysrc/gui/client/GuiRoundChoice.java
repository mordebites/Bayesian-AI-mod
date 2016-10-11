package mc.mod.lilysrc.gui.client;

import java.io.IOException;

import mc.mod.lilysrc.MainRegistry;
import mc.mod.lilysrc.gui.ModGuiHandler;
import mc.mod.lilysrc.match.MatchHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiRoundChoice extends GuiScreen {
	private GuiButton a, b;
	private GuiTextField texter;

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
				centered_width - 70, centered_height - 60, 150, 20);
		texter.setMaxStringLength(60);
		texter.setText("How many rounds?");
		this.texter.setFocused(true);

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100,
				centered_height - 24, "5 will do!"));
		this.buttonList.add(this.b = new GuiButton(1, centered_width - 100,
				centered_height + 4, "3 please"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.a) {
			MainRegistry.match.setRoundsNumber(MatchHandler.MAX_ROUNDS);
		}

		if (button == this.b) {
			MainRegistry.match.setRoundsNumber(MatchHandler.MIN_ROUNDS);
		}

		// avvio il timer del tempo
		MainRegistry.match.startMatch();

		// chiudo la gui selezionando una interfaccia con un id non esistente
		// nel gui handler
		ModGuiHandler.closeCurrentGui();
	}
}
