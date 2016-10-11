package mc.mod.lilysrc.gui.keyhandler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

/**
 * Class handling keybindings declarations
 */
public class KeyBindings {
	// declares keybindings
	public static KeyBinding pauseKey, startMatchKey, escKey;

	public static void init() {

		pauseKey = new KeyBinding("Pause Key", Keyboard.KEY_P, "key.categories.mymod");
		startMatchKey = new KeyBinding("Start Match Key", Keyboard.KEY_M, "key.categories.mymod");
		escKey = new KeyBinding("Escape Key", Keyboard.KEY_ESCAPE, "key.categories.mymod");
		ClientRegistry.registerKeyBinding(pauseKey);
	}
}
