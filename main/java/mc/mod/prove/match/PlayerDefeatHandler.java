package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;

public class PlayerDefeatHandler {

	public static void onPlayerDefeat() {
		if (MainRegistry.match.isMatchStarted()
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY) {

			MainRegistry.match.setWinner(MatchHandler.WINNER_LILY);
			MainRegistry.match.stopRound();

			// se i round sono finiti allora finisco il gioco
			// altrimenti comincio un nuovo round
			
			ModGuiHandler.createGui(ModGuiHandler.GUI_LOST_ROUND);

			/*if (MainRegistry.match.getCurrentRound() == MainRegistry.match
					.getRoundsNumber()) {
				MainRegistry.match.stopMatch();
			} else {
				// mostro la gui di perdita round
				ModGuiHandler.createGui(ModGuiHandler.GUI_LOST_ROUND);
			}*/
		}
	}

}
