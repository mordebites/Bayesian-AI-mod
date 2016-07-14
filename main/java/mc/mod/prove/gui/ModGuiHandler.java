package mc.mod.prove.gui;

import mc.mod.prove.gui.client.StartBet;
import mc.mod.prove.gui.client.stats.OnGameStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

// classe gestore delle gui. Si occupa di capire il tipo di richiesta di gui
// e successivamente chiamare la gui in base al tipo di richiesta.

public class ModGuiHandler implements IGuiHandler {
	
	// queste costanti contengono l'id della gui chiamata chiamata dal client
	
	public static final int GUI_START_BET = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	
    	//if (ID == MOD_TILE_ENTITY_GUI)
          //  return new ContainerModTileEntity();
    	
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	if (ID == GUI_START_BET) {
    		System.out.println("Selezionato GUI di inizio scommessa");
    		return new StartBet();
    	}
    	
        return null;
    }
}