package mc.mod.prove;

import java.util.Timer;
import java.util.TimerTask;

import mc.mod.prove.entity.EntityLily;
import mc.mod.prove.entity.EntityLilyMob;
import mc.mod.prove.gui.CommonProxy;
import mc.mod.prove.gui.KeyHandler.KeyBindings;
import mc.mod.prove.gui.KeyHandler.KeyInputHandler;
import mc.mod.prove.gui.client.stats.RenderGuiHandler;
import mc.mod.prove.gui.sounds.SoundHandler;
import mc.mod.prove.match.MatchHandler;
import mc.mod.prove.match.MatchStartHandler;
import mc.mod.prove.match.PlayerAttackHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MainRegistry.MODID, name = MainRegistry.MODNAME, version = MainRegistry.VERSION)
public class MainRegistry {
	public static final String MODID = "MainRegistry";
	public static final String MODNAME = "Main Registry";
	public static final String VERSION = "1.0.0";
	public static MatchHandler match = new MatchHandler();
	public static EntityLilyMob lily;

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
				// se il round e' cominciato, il gioco non e' in pausa
				// e la schermata di countdown non e' mostrata allora
				// faccio scorrere il tempo decrementando i minuti ed i secondi

				if (match.isRoundStarted() && !match.isGamePaused()
						&& match.getCountDownTime() < 1) {

					match.setSecsTime(match.getSecsTime() - 1);

					if (match.getSecsTime() < 0) {
						match.setSecsTime(59);
						match.setMinutesTime(match.getMinutesTime() - 1);
					}

					if (match.getMinutesTime() < 0) {
						match.stopRound();

						// se i round sono finiti allora finisco il gioco
						// altrimenti comincio un nuovo round

						if (match.getCurrentRound() == match.getRoundsNumber()) {
							match.stopMatch();
						}
					}
				}

				// questo controllo mi servirà per mostrare correttamente la
				// schermata di countdown

				if (match.isMatchStarted() && !match.isRoundStarted() && !match.isGamePaused()) {
					match.setCountDownTime(match.getCountDownTime() - 1);

					if (match.getCountDownTime() < 1) {
						match.startRound();
					}
				}
			}
		}, 0, 1000);

		// inizializzo le interfacce full screen
		CommonProxy.init(e);

		// inizializzo le interfacce widgets che verranno mostrate mentre il
		// player gioca
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
		
		// inizializzo il codice per controllare se l'entity sta per essere attaccata
		MinecraftForge.EVENT_BUS.register(new PlayerAttackHandler());
		
		//MinecraftForge.EVENT_BUS.register(new MatchStartHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		
	}
}