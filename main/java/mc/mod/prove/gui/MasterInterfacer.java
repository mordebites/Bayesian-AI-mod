package mc.mod.prove.gui;

import mc.mod.prove.gui.KeyHandler.KeyBindings;
import mc.mod.prove.gui.KeyHandler.KeyInputHandler;
import mc.mod.prove.gui.client.stats.RenderGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// La mod che corrisponde alla gui

@Mod(modid = "gui", name = "MasterInterfacer", version = "1.0.0")
public class MasterInterfacer {

	public static int ticks = 0;
	public static int secs = 0;
	public static int suspect_percentage = 0;

	@Mod.Instance("gui")
	public static MasterInterfacer instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		// registro il keybinding per i tasti speciali
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		KeyBindings.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		CommonProxy.init(e);

		// registro l'handler che si occupa dei widgets
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
	}
}