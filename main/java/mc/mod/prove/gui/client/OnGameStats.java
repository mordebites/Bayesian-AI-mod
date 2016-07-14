package mc.mod.prove.gui.client;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class OnGameStats extends GuiScreen {
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
	    return false;
	}
	
	@Override
	public void initGui() {
		int centered_width = (this.width / 2);
		int centered_height = (this.height / 2);
		
		this.texter = new GuiTextField(0, this.fontRendererObj, centered_width - 68, centered_height - 60, 150, 20);
        texter.setMaxStringLength(60);
        texter.setText("PASSIVA :3");
        this.texter.setFocused(true);
	}
}
