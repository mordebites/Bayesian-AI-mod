package mc.mod.prove;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class DropHandler {
	public static Random random;
	public static int dropped;

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		random = new Random();
		dropped = random.nextInt(2) + 1; // DO NOT CHANGE THIS

		event.getEntityLiving().entityDropItem(new ItemStack(Items.diamond_axe),
					dropped);
	}
}