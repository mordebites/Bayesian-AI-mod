package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;

public class PlayerDefeatHandler {

	public static void onPlayerDefeat() {
		if (MainRegistry.match.isMatchStarted()
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY) {

			MainRegistry.match.setWinner(MatchHandler.WINNER_LILY);
			MainRegistry.match.stopRound();

			ModGuiHandler.createGui(ModGuiHandler.GUI_LOST_ROUND);

		}
	}

}
