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
					} else if (!isInsideLabirynth((int)event.getHitVec().xCoord, (int)event.getHitVec().zCoord, TYPE_EGG)
							|| event.getHitVec().yCoord > 5
							|| event.getEntityPlayer().posZ <= MainRegistry.LAB_PLATE
									.getZ()) {

						message = "Puoi spawnare Lily solo DENTRO il labirinto! "
								+ "Le coordinate della porta sono [187, 4, 693]";

						event.setCanceled(true);
						// se è nel labirinto
					} else {
						message = "Premi M per giocare! \nUsa P per mettere in pausa durante la partita!"
								+ "\nRicorda: devi essere molto vicino a Lily per colpirla!";
					}

					if (!this.messageSent) {
						event.getEntityPlayer().addChatMessage(
								new TextComponentString(message));
						this.messageSent = true;
					} else {
						this.messageSent = false;
					}
				
				} else {
					if(isInsideLabirynth((int)event.getHitVec().xCoord, (int)event.getHitVec().zCoord, TYPE_OTHER)) {
						event.setCanceled(true);
						
						if (!this.messageSent) {
							event.getEntityPlayer().addChatComponentMessage(
									new TextComponentString("Puoi usare solo l'uovo di Lily nel labirinto!"));
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
				event.getEntityPlayer().addChatMessage(new TextComponentString("Puoi colpire solo Lily nel labirinto!"));
				this.messageSent = true;
			} else {
				this.messageSent = false;
			}
		}
	}

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
