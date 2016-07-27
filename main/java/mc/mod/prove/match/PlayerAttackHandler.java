package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.EntityLilyMob;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAttackHandler {
	@SubscribeEvent
	public void onAttack(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityLilyMob
				&& MainRegistry.match.isMatchStarted()
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY) {
			
			// mostro la schermata di vittoria
			ModGuiHandler.createGui(ModGuiHandler.GUI_VICTORY);
			
			MainRegistry.match.setWinner(MatchHandler.WINNER_PLAYER);
			
			EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;
			AwardHandler.addItem(playerIn, AwardHandler.EMERALDS_TO_WIN);
			MainRegistry.match.stopRound();
			
			// se i round sono finiti allora finisco il gioco
			// altrimenti comincio un nuovo round

			if (MainRegistry.match.getCurrentRound() == MainRegistry.match.getRoundsNumber()) {
				MainRegistry.match.stopMatch();
			}
		}
	}
}
