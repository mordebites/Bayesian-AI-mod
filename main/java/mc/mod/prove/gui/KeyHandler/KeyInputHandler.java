package mc.mod.prove.gui.KeyHandler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (KeyBindings.ping.isPressed())
			System.out.println("ping");
		if (KeyBindings.pong.isPressed())
			System.out.println("pong");
	}
}
