package mc.mod.prove.gui.client;

import java.io.IOException;

import mc.mod.prove.gui.MasterInterfacer;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

public class RoundChoice extends GuiScreen {
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
		texter.setText("How many rounds?");
		this.texter.setFocused(true);

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100,
				centered_height - 24, "5 and I'm in!"));
		this.buttonList.add(this.b = new GuiButton(1, centered_width - 100,
				centered_height + 4, "3 pls"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.a) {
			System.out.println("5");

			MasterInterfacer.maxRound = 5;
		}

		if (button == this.b) {
			System.out.println("3");

			MasterInterfacer.maxRound = 3;
		}

		// avvio il timer del tempo
		MasterInterfacer.matchStarted = true;

		// chiudo la gui selezionando una interfaccia con un id non esistente
		// nel gui handler
		ModGuiHandler.closeCustomGui();

		// TODO CODICE PER CAMBIARE LA POSIZIONE CORRENTE DEL PLAYER E ALZARLO
		// sull'asse y
		EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;

		playerIn.setPositionAndUpdate(playerIn.posX, playerIn.posY + 40,
				playerIn.posZ);
	}
}
