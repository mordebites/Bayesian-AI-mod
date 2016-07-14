package mc.mod.prove.gui;

import mc.mod.prove.gui.client.stats.RenderGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

// classe mod che corrisponde alla gui

@Mod(modid = "gui", name = "MasterInterfacer", version = "1.0.0")
public class MasterInterfacer {

	@Mod.Instance("gui")
	public static MasterInterfacer instance;
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		CommonProxy.init(e);
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());

	}
}