package mc.mod.prove.gui.keyhandler;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;
import mc.mod.prove.match.AwardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Class handling the logic to be executed when certain keys are pressed
 */
public class KeyInputHandler {
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (KeyBindings.pauseKey.isPressed()) {
			System.out.println("Pressed P");

			if (MainRegistry.match.isMatchStarted()) {
				ModGuiHandler.createGui(ModGuiHandler.GUI_STOP_MATCH);
			}
		}

		if (KeyBindings.startMatchKey.isPressed()) {
			System.out.println("Pressed M");

			if (MainRegistry.lily == null) {
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
					new TextComponentString("Spawn the yellow and pink egg in the labyrinth, then press M to play!"));
			} else {
				if (!MainRegistry.match.isMatchStarted()) {
					if (AwardHandler.hasEmeralds(Minecraft.getMinecraft().thePlayer,
							AwardHandler.EMERALDS_TO_PLAY)) {
						ModGuiHandler.createGui(ModGuiHandler.GUI_START_BET);
					} else {
						ModGuiHandler
								.createGui(ModGuiHandler.GUI_NOT_ENOUGH_MONEY);
					}
				}
			}
		}
		
		if (KeyBindings.escKey.isPressed()) {
			if (MainRegistry.match.isMatchStarted()) {
				ModGuiHandler.createGui(ModGuiHandler.GUI_STOP_MATCH);
			}
		}
	}
}
