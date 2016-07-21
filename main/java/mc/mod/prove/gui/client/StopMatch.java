package mc.mod.prove.gui.client;

import java.io.IOException;

import mc.mod.prove.gui.MasterInterfacer;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class StopMatch extends GuiScreen {
	private GuiButton a, b;
	private GuiTextField texter;

	public void GuyTutorial() {
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
				centered_width - 70, centered_height - 60, 150, 20);
		texter.setMaxStringLength(60);
		texter.setText("are you a chicken McFly?");
		this.texter.setFocused(true);

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100,
				centered_height - 24, "Yes"));
		this.buttonList.add(this.b = new GuiButton(1, centered_width - 100,
				centered_height + 4, "Nobody calls me CHICKEN!"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.a) {
			MasterInterfacer.matchStarted = false;
			MasterInterfacer.reloadTimer();
		}

		if (button == this.b) {
			System.out.println("*SBAM*");
		}

		// chiudo la gui selezionando una interfaccia con un id non esistente
		// nel gui handler
		ModGuiHandler.closeCustomGui();
	}
}
