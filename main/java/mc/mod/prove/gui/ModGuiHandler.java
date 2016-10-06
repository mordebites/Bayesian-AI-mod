package mc.mod.prove.gui;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.client.GuiInstructions;
import mc.mod.prove.gui.client.GuiLostMatch;
import mc.mod.prove.gui.client.GuiLostRound;
import mc.mod.prove.gui.client.GuiMatchWon;
import mc.mod.prove.gui.client.GuiNotEnoughMoney;
import mc.mod.prove.gui.client.GuiRoundChoice;
import mc.mod.prove.gui.client.GuiRoundWon;
import mc.mod.prove.gui.client.GuiStartBet;
import mc.mod.prove.gui.client.GuiStopMatch;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

// Class that handles the GUI. It figures the type of GUI request
// and then calls the appropriate GUI.

public class ModGuiHandler implements IGuiHandler {
	//constants with the GUI IDs
	public static final int GUI_START_BET = 0;
	public static final int GUI_ROUND_CHOICE = 1;
	public static final int GUI_STOP_MATCH = 2;
	public static final int GUI_NOT_ENOUGH_MONEY = 3;
	public static final int GUI_WON_ROUND = 4;
	public static final int GUI_LOST_ROUND = 5;
	public static final int GUI_WON_MATCH = 6;
	public static final int GUI_LOST_MATCH = 7;
	public static final int GUI_INSTRUCTIONS = 8;

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

		switch (ID) {
		case GUI_START_BET: {
			System.out.println("Selezionato GUI di inizio scommessa");
			return new GuiStartBet();
		}
		case GUI_ROUND_CHOICE: {
			System.out.println("Selezionato GUI di selezione round");
			return new GuiRoundChoice();
		}
		case GUI_STOP_MATCH: {
			System.out.println("selezionato GUI interruzione match");
			return new GuiStopMatch();
		}
		case GUI_NOT_ENOUGH_MONEY: {
			System.out.println("selezionato GUI di errore soldi disponibili");
			return new GuiNotEnoughMoney();
		}
		case GUI_WON_ROUND: {
			System.out.println("selezionato GUI di vittoria round!");
			return new GuiRoundWon();
		}
		case GUI_LOST_ROUND: {
			System.out.println("selezionato GUI  di perdita round");
			return new GuiLostRound();
		}
		case GUI_WON_MATCH: {
			System.out.println("selezionato GUI di vittoria match!");
			return new GuiMatchWon();
		}
		case GUI_LOST_MATCH: {
			System.out.println("selezionato GUI  di perdita match");
			return new GuiLostMatch();
		}
		case GUI_INSTRUCTIONS: {
			System.out.println("selezionato GUI  delle istruzioni");
			return new GuiInstructions();
		}
		}

		return null;
	}

	/*
	 * Closes the current gui by trying to open a gui with an invalid ID.
	 * Needs fixing. 
	 */
	public static void closeCurrentGui() {
		createGui(-1);
	}

	public static void createGui(int id) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = Minecraft.getMinecraft().theWorld;
		player.openGui(MainRegistry.modInstance, id, world, (int) player.posX,
				(int) player.posY, (int) player.posZ);
	}
}