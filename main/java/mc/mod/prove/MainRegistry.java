package mc.mod.prove;

import mc.mod.prove.entity.EntityLily;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MainRegistry.MODID, version = MainRegistry.VERSION)
public class MainRegistry {
	public static final String MODID = "ProvaMob";
	public static final String VERSION = "1.0";
	public static int timer = 6000;

	@Instance(MODID)
	public static MainRegistry modInstance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		EntityLily.mainRegistry();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		//MinecraftForge.EVENT_BUS.register(new LoginHandler());
		//MinecraftForge.EVENT_BUS.register(new PlateClickHandler());
		MinecraftForge.EVENT_BUS.register(new AttackHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		
	}
}