package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.EntityLilyMob;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAttackHandler {
	@SubscribeEvent
	public void onAttack(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		int distance = (int) entity.getPositionVector().distanceTo(player.getPositionVector());
		
		if (entity instanceof EntityLilyMob
				&& MainRegistry.match.isMatchStarted()
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY
				&& distance <= 1) {

			MainRegistry.match.setWinner(MatchHandler.WINNER_PLAYER);

			// incremento il numero di round vinti
			MainRegistry.match
					.setRoundsWon(MainRegistry.match.getRoundsWon() + 1);
			MainRegistry.match.stopRound();

			// se i round sono finiti allora finisco il gioco
			// altrimenti comincio un nuovo round

			if (MainRegistry.match.getCurrentRound() == MainRegistry.match
					.getRoundsNumber()) {
				MainRegistry.match.stopMatch();
			} else {
				// mostro la schermata di vittoria
				ModGuiHandler.createGui(ModGuiHandler.GUI_WON_ROUND);
			}
		}
	}
}
