package mc.mod.prove.match;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;

public class PlayerDefeatHandler {

	public static void onPlayerDefeat() {
		if (MainRegistry.match.isMatchStarted()
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY) {
			
			ModGuiHandler.createGui(ModGuiHandler.GUI_LOSE);

			MainRegistry.match.setWinner(MatchHandler.WINNER_LILY);

			EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;
			AwardHandler.removeItem(playerIn, AwardHandler.EMERALDS_TO_LOSE);
			MainRegistry.match.stopRound();

			// se i round sono finiti allora finisco il gioco
			// altrimenti comincio un nuovo round

			if (MainRegistry.match.getCurrentRound() == MainRegistry.match
					.getRoundsNumber()) {
				MainRegistry.match.stopMatch();
			}
		}
	}

}
