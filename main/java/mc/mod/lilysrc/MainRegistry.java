package mc.mod.lilysrc;

import java.util.Timer;
import java.util.TimerTask;

import mc.mod.lilysrc.entity.EntityLily;
import mc.mod.lilysrc.entity.EntityLilyMob;
import mc.mod.lilysrc.eventhandler.PlayerAttackHandler;
import mc.mod.lilysrc.eventhandler.PlayerLogHandler;
import mc.mod.lilysrc.eventhandler.PlayerMouseHandler;
import mc.mod.lilysrc.gui.CommonProxy;
import mc.mod.lilysrc.gui.client.stats.RenderGuiHandler;
import mc.mod.lilysrc.gui.keyhandler.KeyBindings;
import mc.mod.lilysrc.gui.keyhandler.KeyInputHandler;
import mc.mod.lilysrc.gui.sounds.SoundHandler;
import mc.mod.lilysrc.match.MatchHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Main class of the mod. The execution of the mod, since its preinitialization,
 * starts here.
 */

@Mod(modid = MainRegistry.MODID, name = MainRegistry.MODNAME, version = MainRegistry.VERSION)
public class MainRegistry {
	public static final String MODID = "MainRegistry";
	public static final String MODNAME = "Main Registry";
	public static final String VERSION = "1.0.0";
	public static MatchHandler match = new MatchHandler();
	// reference to the NPC the mod revolves around
	public static EntityLilyMob lily;
	// labyrinth coordinates
	public static final int MIN_X_LAB = 178;
	public static final int MAX_X_LAB = 199;
	public static final int MIN_Z_LAB = 694;
	public static final int MAX_Z_LAB = 716;
	// coordinates of the pressure plate in front of the labyrinth's door
	public static final BlockPos LAB_PLATE = new BlockPos(187, 4, 692);
	// used by methods that handle the content of player's inventory
	public static ItemMonsterPlacer lilyEgg;

	@Instance(MODID)
	public static MainRegistry modInstance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		System.out.println("Preinit!");
		// Initializes the Lily entity 
		EntityLily.mainRegistry();
		lilyEgg = (ItemMonsterPlacer) Item
				.getByNameOrId("Spawn entity.MainRegistry.LilyMob.name");

		// registers the handler for custom sounds
		SoundHandler.init();

		// registers keybinding handler
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		KeyBindings.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		System.out.println("Init!");

		//timer keeping track of the seconds
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				
				//if round is started, game is not paused and the countdown is not active
				// then time passes, minutes and seconds are decreased
				if (match.isRoundStarted() && !match.isGamePaused()
						&& match.getCountDownTime() < 1) {

					match.setSecsTime(match.getSecsTime() - 1);

					if (match.getSecsTime() < 0) {
						match.setSecsTime(59);
						match.setMinutesTime(match.getMinutesTime() - 1);
					}

					if (match.getMinutesTime() < 0) {
						match.stopRound();

						// if rounds are over
						// then the game ends
						// else a new round starts

						if (match.getCurrentRound() == match.getRoundsNumber()) {
							match.stopMatch();
						}
					}
				}

				// this control is used to correctly show the countdown screen
				if (match.isMatchStarted() && !match.isRoundStarted()
						&& !match.isGamePaused()) {
					match.setCountDownTime(match.getCountDownTime() - 1);

					if (match.getCountDownTime() < 1) {
						match.startRound();
					}
				}
			}
		}, 0, 1000);

		// initialization of fullscreen UI
		CommonProxy.init(e);

		// initialization of widget interfaces shown dureing the match 
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());

		// initialization of handler for player attack
		// called when entity is about to be attacked
		MinecraftForge.EVENT_BUS.register(new PlayerAttackHandler());

		MinecraftForge.EVENT_BUS.register(new PlayerMouseHandler());

		MinecraftForge.EVENT_BUS.register(new PlayerLogHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		System.out.println("Postinit!");
	}
}