package mc.mod.prove.gui;

import java.util.Timer;
import java.util.TimerTask;

import mc.mod.prove.gui.KeyHandler.KeyBindings;
import mc.mod.prove.gui.KeyHandler.KeyInputHandler;
import mc.mod.prove.gui.client.stats.RenderGuiHandler;
import mc.mod.prove.gui.sounds.SoundHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// La mod che corrisponde alla gui

@Mod(modid = MasterInterfacer.MODID, version = MasterInterfacer.VERSION)
public class MasterInterfacer {
	public static final String MODID = "MasterInterfacer";
	public static final String VERSION = "1.0.0";

	public static int suspectValue = 0;

	public static int minutesTime = 5;
	public static int secsTime = 0;
	
	public static int maxRound = 5; // default number of rounds
	public static boolean matchStarted = false;

	@Mod.Instance("MasterInterfacer")
	public static MasterInterfacer instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		// registro l'handler che si occupa dei suoni personalizzati
		SoundHandler.init();

		// registro il keybinding per i tasti speciali
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		KeyBindings.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		// timer che tiene traccia dei secondi

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (MasterInterfacer.matchStarted) {
					// TODO il tempo verra' gestito dal gestore dei round

					MasterInterfacer.secsTime -= 1;

					if (MasterInterfacer.secsTime < 0) {
						MasterInterfacer.secsTime = 59;
						MasterInterfacer.minutesTime -= 1;
					}

					// TODO la barra di sospetto non funziona così è solo una
					// prova
					// per vedere se i valori compaiono nell'interfaccia
					MasterInterfacer.suspectValue += 1;
				}
			}
		}, 0, 1000);

		// inizializzo le interfacce full screen
		CommonProxy.init(e);

		// inizializzo le interfacce widgets che verranno mostrate mentre il player gioca
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
	}
	
	// reset timer to default values
	public static void reloadTimer() {
		MasterInterfacer.minutesTime = 5;
		MasterInterfacer.secsTime = 0;
	}
}