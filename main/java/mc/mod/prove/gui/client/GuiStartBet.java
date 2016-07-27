package mc.mod.prove.gui.client;

import java.io.IOException;

import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

// Classe dell'effettiva interfaccia attivata dall'utente
// Questa interfaccia viene attivata quando l'npc ci propone la scommessa

// incolla il seguente metodo nel pezzo di codice che deve aprire la gui
// nei parametri lo zero indica il codice dell'interfaccia che l'handler deve aprire
// player.openGui(MasterInterfacer.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);

public class GuiStartBet extends GuiScreen {
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
		texter.setText("Hey there! Let's battle!!11");
		this.texter.setFocused(true);

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100,
				centered_height - 24, "Yes"));
		this.buttonList.add(this.b = new GuiButton(1, centered_width - 100,
				centered_height + 4, "Nope"));
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
	}
}