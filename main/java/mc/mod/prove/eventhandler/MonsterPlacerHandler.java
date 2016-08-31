package mc.mod.prove.eventhandler;

import mc.mod.prove.MainRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonsterPlacerHandler {
	private boolean messageSent = false;

	@SubscribeEvent
	public void onEvent(RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		if (stack != null) {
			Item item = stack.getItem();
			if (item != null && item instanceof ItemMonsterPlacer) {
				ItemMonsterPlacer egg = (ItemMonsterPlacer) event
						.getItemStack().getItem();
				System.out.println(egg.getItemStackDisplayName(stack));

				if (egg.getEntityIdFromItem(event.getItemStack()).contains(
						"Lily")) {
					if (MainRegistry.lilyEgg == null) {
						MainRegistry.lilyEgg = egg;
						System.out.println("Lily's egg added");
					}
					String message = "";

					// se c'è già una Lily
					if (MainRegistry.lily != null) {
						message = "Puoi spawnare solo una Lily!";
						event.setCanceled(true);

						// se non è nel labirinto
					} else if (event.getHitVec().xCoord < MainRegistry.MIN_X_LAB
							|| event.getHitVec().xCoord > MainRegistry.MAX_X_LAB
							|| event.getHitVec().zCoord < MainRegistry.MIN_Z_LAB
							|| event.getHitVec().zCoord > MainRegistry.MAX_Z_LAB
							|| event.getHitVec().yCoord > 5
							|| event.getEntityPlayer().posZ <= MainRegistry.LAB_PLATE
									.getZ()) {

						message = "Puoi spawnare Lily solo DENTRO il labirinto! "
								+ "Le coordinate della porta sono [187, 4, 693]";

						event.setCanceled(true);
						// se è nel labirinto
					} else {
						message = "Premi M per giocare! \nUsa P per mettere in pausa durante la partita!";
					}

					if (!this.messageSent) {
						event.getEntityPlayer().addChatMessage(
								new TextComponentString(message));
						this.messageSent = true;
					} else {
						this.messageSent = false;
					}

				}

			}
		}
	}

}
