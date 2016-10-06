package mc.mod.prove.eventhandler;

import mc.mod.prove.MainRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class that handles left and right click.
 */
public class PlayerMouseHandler {
	private static final int TYPE_EGG = 0;
	private static final int TYPE_OTHER = 2;
	private boolean messageSent = false;

	@SubscribeEvent
	public void onRightClickEvent(RightClickBlock event) {
		boolean isLily = false;
		ItemMonsterPlacer egg = null;
		
		ItemStack stack = event.getItemStack();
		if (stack != null) {
			Item item = stack.getItem();
			if (item != null) {
				if(item instanceof ItemMonsterPlacer) {
					egg = (ItemMonsterPlacer) event.getItemStack().getItem();
					isLily = egg.getEntityIdFromItem(event.getItemStack()).contains("Lily");
				}
				
				if(isLily) {
					String message = "";

					//if Lily has already been spawned
					if (MainRegistry.lily != null) {
						message = "You can spawn just one Lily!";
						event.setCanceled(true);
						// if the hit vector is not in the labyrinth
					} else if (!isInsideLabirynth((int)event.getHitVec().xCoord, (int)event.getHitVec().zCoord, TYPE_EGG)
							|| event.getHitVec().yCoord > 5
							|| event.getEntityPlayer().posZ <= MainRegistry.LAB_PLATE
									.getZ()) {

						message = "You can spawn Lily only INSIDE the labyrinth! "
								+ "The coordinates of the door are [187, 4, 690]";

						event.setCanceled(true);
						//  if the hit vector is in the labyrinth
					} else {
						message = "Press M to play! \nPress P or ESC to pause the match!"
								+ "\n\nREMEMBER: you must be REALLY CLOSE to Lily to hit her!";
					}

					if (!this.messageSent) {
						event.getEntityPlayer().addChatMessage(
								new TextComponentString(message));
						this.messageSent = true;
					} else {
						this.messageSent = false;
					}
				
					//if it's not Lily
				} else {
					if(isInsideLabirynth((int)event.getHitVec().xCoord, (int)event.getHitVec().zCoord, TYPE_OTHER)) {
						event.setCanceled(true);
						
						if (!this.messageSent) {
							event.getEntityPlayer().addChatComponentMessage(
									new TextComponentString("You can use only Lily's egg in the labyrinth!"));
							this.messageSent = true;
						} else {
							this.messageSent = false;
						}	
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLeftClickEvent(LeftClickBlock event) {
		Vec3d hit = event.getHitVec();

		if (this.isInsideLabirynth((int) hit.xCoord, (int) hit.zCoord, TYPE_OTHER)) {
			event.setCanceled(true);
			
			if (!this.messageSent) {
				event.getEntityPlayer().addChatMessage(new TextComponentString("You can hit only Lily in the labyrinth!"));
				this.messageSent = true;
			} else {
				this.messageSent = false;
			}
		}
	}

	//checks whether the coordinates are inside the labirinth
	private boolean isInsideLabirynth(int x, int z, int type) {
		boolean result = false;
		if (x >= MainRegistry.MIN_X_LAB - type && x <= MainRegistry.MAX_X_LAB + type
			&& z >= MainRegistry.MIN_Z_LAB - type && z <= MainRegistry.MAX_Z_LAB + type) {
			result = true;
		}
		System.out.println("Inside labirynth?: " + result);
		return result;
	}
}
