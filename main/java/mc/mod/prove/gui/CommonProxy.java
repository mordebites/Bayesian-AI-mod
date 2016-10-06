package mc.mod.prove.gui;

import mc.mod.prove.MainRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * A class used to register the GUI for the mod to the GUI handler
 */

public class CommonProxy {
	public static void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MainRegistry.modInstance, new ModGuiHandler());
	}
}
