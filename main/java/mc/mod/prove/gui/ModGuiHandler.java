package mc.mod.prove.gui;

import mc.mod.prove.gui.client.RoundChoice;
import mc.mod.prove.gui.client.StartBet;
import mc.mod.prove.gui.client.StopMatch;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

// classe gestore delle gui. Si occupa di capire il tipo di richiesta di gui
// e successivamente chiamare la gui in base al tipo di richiesta.

public class ModGuiHandler implements IGuiHandler {

	// queste costanti contengono l'id della gui chiamata chiamata dal client

	public static final int GUI_START_BET = 0;
	public static final int GUI_ROUND_CHOICE = 1;
	public static final int GUI_STOP_MATCH = 2;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {

		// if (ID == MOD_TILE_ENTITY_GUI)
		// return new ContainerModTileEntity();

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID == GUI_START_BET) {
			System.out.println("Selezionato GUI di inizio scommessa");
			return new StartBet();
		} else if (ID == GUI_ROUND_CHOICE) {
			System.out.println("Selezionato GUI di selezione round");
			return new RoundChoice();
		} else if (ID == GUI_STOP_MATCH) {
			System.out.println("selezionato gui interruzione match");
			return new StopMatch();
		}

		return null;
	}

	// questo metodo ci consente di chiudere una gui cercando di aprire una gui
	// con un id
	// nel gui handler che non esiste, quindi openGui ritornerà null ritornando
	// alla schermata
	// di gioco. (e' una brutta soluzione poi si fixa)

	// TODO una maniera piu' bella per chiudere le gui
	public static void closeCustomGui() {
		createGui(-1);
	}

	public static void createGui(int id) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = Minecraft.getMinecraft().theWorld;
		player.openGui(MasterInterfacer.instance, id, world, (int) player.posX,
				(int) player.posY, (int) player.posZ);
	}
}