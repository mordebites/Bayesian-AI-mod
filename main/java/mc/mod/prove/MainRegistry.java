package mc.mod.prove;

import java.util.Timer;
import java.util.TimerTask;

import mc.mod.prove.entity.EntityLily;
import mc.mod.prove.gui.CommonProxy;
import mc.mod.prove.gui.KeyHandler.KeyBindings;
import mc.mod.prove.gui.KeyHandler.KeyInputHandler;
import mc.mod.prove.gui.client.stats.RenderGuiHandler;
import mc.mod.prove.gui.sounds.SoundHandler;
import mc.mod.prove.match.MatchHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MainRegistry.MODID, version = MainRegistry.VERSION)
public class MainRegistry {
	public static final String MODID = "MainRegistry";
	public static final String VERSION = "1.0.0";
	public static MatchHandler match = new MatchHandler();

	@Instance(MODID)
	public static MainRegistry modInstance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		// Inizializza l'entity lily
		EntityLily.mainRegistry();

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
				if (match.isRoundStarted() && !match.isGamePaused()) {
					
					// faccio scorrere il tempo decrementando correttamente
					// i minuti ed i secondi
					match.setSecsTime(match.getSecsTime() - 1);

					if (match.getSecsTime() < 0) {
						match.setSecsTime(59);
						match.setMinutesTime(match.getMinutesTime() - 1);
					}

					// TODO la barra di sospetto non funziona così è solo una
					// prova
					// per vedere se i valori compaiono nell'interfaccia
					//match.setSightValue(match.getSightValue() + 1);
				}
			}
		}, 0, 1000);

		// inizializzo le interfacce full screen
		CommonProxy.init(e);

		// inizializzo le interfacce widgets che verranno mostrate mentre il
		// player gioca
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {

	}
}