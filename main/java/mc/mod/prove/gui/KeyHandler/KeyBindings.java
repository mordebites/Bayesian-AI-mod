package mc.mod.prove.gui.KeyHandler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

public class KeyBindings {
	// Declare two KeyBindings, ping and pong
	public static KeyBinding pauseKey, startMatchKey;

	public static void init() {

		pauseKey = new KeyBinding("Pause Key", Keyboard.KEY_P, "key.categories.mymod");
		startMatchKey = new KeyBinding("Start Match Key", Keyboard.KEY_M, "key.categories.mymod");
		
		ClientRegistry.registerKeyBinding(pauseKey);
	}
}
