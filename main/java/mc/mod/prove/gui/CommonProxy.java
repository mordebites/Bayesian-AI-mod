package mc.mod.prove.gui;

import mc.mod.prove.MainRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

// common proxy è una classe usata per registrare la mod gui con l'handler della gui

public class CommonProxy {
	public static void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MasterInterfacer.instance, new ModGuiHandler());
	}
}
