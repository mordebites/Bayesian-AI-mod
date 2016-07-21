package mc.mod.prove.gui.KeyHandler;

import mc.mod.prove.gui.MasterInterfacer;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (KeyBindings.pauseKey.isPressed()) {
			System.out.println("Pressed P");
			
			if (MasterInterfacer.matchStarted) {
				ModGuiHandler.createGui(ModGuiHandler.GUI_STOP_MATCH);
			} else {
				ModGuiHandler.createGui(ModGuiHandler.GUI_START_BET);
			}
		}
	}
}
