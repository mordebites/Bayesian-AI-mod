package mc.mod.prove.gui;

import java.util.Timer;
import java.util.TimerTask;

import mc.mod.prove.gui.KeyHandler.KeyBindings;
import mc.mod.prove.gui.KeyHandler.KeyInputHandler;
import mc.mod.prove.gui.client.stats.OnGameStats;
import mc.mod.prove.gui.client.stats.RenderGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// La mod che corrisponde alla gui

@Mod(modid = "gui", name = "MasterInterfacer", version = "1.0.0")
public class MasterInterfacer {

	public static int suspectPercentage = 0;
	public static long generalTime = 0;
	public static boolean matchStarted = false;

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

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (MasterInterfacer.matchStarted) {
					MasterInterfacer.generalTime += 1;
				}
			}
		}, 0, 1000);

		CommonProxy.init(e);

		// registro l'handler che si occupa dei widgets
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
	}
}