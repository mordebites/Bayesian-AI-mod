package mc.mod.prove.gui.client;

import java.io.IOException;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiInstructions extends GuiScreen {
	private GuiButton a;
	private GuiTextField t1, t2, t3, t4, t5, t6;

	public GuiInstructions() {
		MainRegistry.match.setGamePaused(true);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.t1.drawTextBox();
		this.t2.drawTextBox();
		this.t3.drawTextBox();
		this.t4.drawTextBox();
		this.t5.drawTextBox();
		this.t6.drawTextBox();
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
		int textWidth = (this.width/6)*5;

		this.t1 = new GuiTextField(0, this.fontRendererObj,	centered_width - textWidth/2, 60, textWidth, 20);
		t1.setMaxStringLength(80);
		this.t2 = new GuiTextField(0, this.fontRendererObj,	centered_width - textWidth/2, t1.yPosition + 21, textWidth, 20);
		t2.setMaxStringLength(80);
		this.t3 = new GuiTextField(0, this.fontRendererObj, centered_width - textWidth/2, t2.yPosition + 21, textWidth, 20);
		t3.setMaxStringLength(80);
		this.t4 = new GuiTextField(0, this.fontRendererObj,	centered_width - textWidth/2, t3.yPosition + 21, textWidth, 20);
		t4.setMaxStringLength(80);
		this.t5 = new GuiTextField(0, this.fontRendererObj,	centered_width - textWidth/2, t4.yPosition + 21, textWidth, 20);
		t5.setMaxStringLength(80);
		this.t6 = new GuiTextField(0, this.fontRendererObj,	centered_width - textWidth/2, t5.yPosition + 21, textWidth, 20);
		t6.setMaxStringLength(80);
		
		t1.setText("Per vincere un round dovrai trovarmi nel labirinto e colpirmi senza che ");
		t2.setText("io ti scopra! Chi vince il maggior numero di round vince la partita.");
		t3.setText("Una barra in alto a sinistra ti indicherà se hai attirato la mia attenzione");
		t4.setText("e in che misura. Ogni volta che ti individuo un suono ti avviserà!");
		t5.setText("Se vinci, guadagni 2 Smeraldi per ogni round vinto, mentre se perdi");
		t6.setText("ne perderai uno per ogni round in cui ti ho sconfitto. Buona partita!");

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100, t6.yPosition + 35, "Indietro"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.a) {
			ModGuiHandler.createGui(ModGuiHandler.GUI_START_BET);
		}
	}

	@Override
	public void onGuiClosed() {
		System.out.println("Ripristino lo stato di PAUSED");
		// tolgo il gioco dallo stato di pausa
		MainRegistry.match.setGamePaused(false);
	}
}
