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

/**
 * Class that handles an attack from the player.
 */
public class PlayerAttackHandler {
	@SubscribeEvent
	public void onAttack(LivingHurtEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			EntityLivingBase entity = event.getEntityLiving();

			//the event is handled only if Lily is about to be hit
			if (entity instanceof EntityLilyMob) {
				if (MainRegistry.match.isMatchStarted()) {
					
					if (MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY) {

						MainRegistry.match
								.setWinner(MatchHandler.WINNER_PLAYER);

						// increase number of rounds won
						MainRegistry.match.setRoundsWon(MainRegistry.match
								.getRoundsWon() + 1);
						MainRegistry.match.stopRound();

						// if rounds are over then the game is over
						// else a new round starts

						ModGuiHandler
						.createGui(ModGuiHandler.GUI_WON_ROUND);
					}
				}
			}
		}

	}

}
