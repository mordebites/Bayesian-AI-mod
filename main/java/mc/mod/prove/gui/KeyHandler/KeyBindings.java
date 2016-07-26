package mc.mod.prove.gui.keyhandler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

public class KeyBindings {
	// Declare two KeyBindings, ping and pong
	public static KeyBinding pauseKey;

	public static void init() {

		pauseKey = new KeyBinding("Pause Key", Keyboard.KEY_P, "key.categories.mymod");
		
		ClientRegistry.registerKeyBinding(pauseKey);
	}
}
