package mc.mod.prove.eventhandler;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.EntityLilyMob;
import mc.mod.prove.gui.ModGuiHandler;
import mc.mod.prove.match.MatchHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAttackHandler {
	@SubscribeEvent
	public void onAttack(LivingHurtEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			EntityLivingBase entity = event.getEntityLiving();

			double distance = entity.getPositionVector().distanceTo(
					player.getPositionVector());

			if (entity instanceof EntityLilyMob) {
				if (MainRegistry.match.isMatchStarted()) {
					if (MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY
							&& distance <= 1) {

						MainRegistry.match
								.setWinner(MatchHandler.WINNER_PLAYER);

						// incremento il numero di round vinti
						MainRegistry.match.setRoundsWon(MainRegistry.match
								.getRoundsWon() + 1);
						MainRegistry.match.stopRound();

						// se i round sono finiti allora finisco il gioco
						// altrimenti comincio un nuovo round

						if (MainRegistry.match.getCurrentRound() == MainRegistry.match
								.getRoundsNumber()) {
							MainRegistry.match.stopMatch();
						} else {
							// mostro la schermata di vittoria
							ModGuiHandler
									.createGui(ModGuiHandler.GUI_WON_ROUND);
						}
					}
				} /*else {
					if (event.isCancelable()) {
						event.setCanceled(true);
					}
				}*/
			}
		}

	}

}
