package mc.mod.lilysrc.gui.client;

import java.io.IOException;

import mc.mod.lilysrc.gui.ModGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiNotEnoughMoney extends GuiScreen {
	private GuiButton okay_button;
	private GuiTextField texter;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.texter.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		int centered_width = (this.width / 2);
		int centered_height = (this.height / 2);

		this.texter = new GuiTextField(0, this.fontRendererObj,
				centered_width - 120, centered_height - 60, 240, 20);
		texter.setMaxStringLength(90);
		texter.setText("You must have at least 5 Emeralds to fight me!");
		this.texter.setFocused(true);

		this.buttonList.add(this.okay_button = new GuiButton(0, centered_width - 100,
				centered_height - 24, "See ya"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.okay_button) {
			ModGuiHandler.closeCurrentGui();
		}
	}
}
