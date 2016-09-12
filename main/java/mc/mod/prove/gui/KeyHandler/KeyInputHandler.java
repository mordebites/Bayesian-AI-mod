package mc.mod.prove.gui.KeyHandler;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;
import mc.mod.prove.match.AwardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

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
					new TextComponentString("Devi usare l'uovo giallo e rosa nel labirinto, poi premi M per giocare!"));
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

		if (KeyBindings.locationKey.isPressed()) {
			if (MainRegistry.lily != null) {
				System.out.println("Requested Lily's Position: "
						+ MainRegistry.lily.posX + ", "
						+ MainRegistry.lily.posZ);
			}
		}
		
		if (KeyBindings.escKey.isPressed()) {
			if (MainRegistry.match.isMatchStarted()) {
				ModGuiHandler.createGui(ModGuiHandler.GUI_STOP_MATCH);
			}
		}
	}
}
