package mc.mod.lilysrc.gui.client;

import java.io.IOException;

import mc.mod.lilysrc.MainRegistry;
import mc.mod.lilysrc.gui.ModGuiHandler;
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
		
		t1.setText("To win a round you must find me and hit me without me ");
		t2.setText("noticing you! The one who wins most of the rounds wins the match! ");
		t3.setText("A bar in the upper side of the screen will tell you if I saw you and my ");
		t4.setText("alert level. Each time I found you a sound will inform you! ");
		t5.setText("If you win, you win 2 Emeralds for each won round, while if you lose ");
		t6.setText("you will lose one for each round I defeated you. Enjoy the match!");

		this.buttonList.add(this.a = new GuiButton(0, centered_width - 100, t6.yPosition + 35, "Back"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.a) {
			ModGuiHandler.createGui(ModGuiHandler.GUI_START_BET);
		}
	}

	@Override
	public void onGuiClosed() {
		// tolgo il gioco dallo stato di pausa
		MainRegistry.match.setGamePaused(false);
	}
}
