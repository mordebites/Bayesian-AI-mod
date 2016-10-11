package mc.mod.lilysrc.gui.client;

import java.io.IOException;

import mc.mod.lilysrc.gui.ModGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

// Class for the user activated interface.
// Called when the NPC starts the bet

public class GuiStartBet extends GuiScreen {
	private GuiButton a, b, c;
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
		texter.setText("Wanna fight me?");
		this.texter.setFocused(true);

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100,
				centered_height - 24, "Sure!"));
		this.buttonList.add(this.b = new GuiButton(1, centered_width - 100,
				centered_height + 4, "Not now..."));
		this.buttonList.add(this.c = new GuiButton(1, centered_width - 100,
				centered_height + 32, "Game rules?"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.a) {
			System.out.println("Yes chosen!");

			ModGuiHandler.createGui(ModGuiHandler.GUI_ROUND_CHOICE);
		}
		if (button == this.b) {
			System.out.println("Nope chosen!");

			ModGuiHandler.closeCurrentGui();
		}
		
		if(button == this.c) {
			System.out.println("Instructions chosen!");

			ModGuiHandler.createGui(ModGuiHandler.GUI_INSTRUCTIONS);
		}
	}
}