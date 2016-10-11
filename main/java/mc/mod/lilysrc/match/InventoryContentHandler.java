package mc.mod.lilysrc.match;

import mc.mod.lilysrc.MainRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Class that handles the content of player's inventory.
 */
public class InventoryContentHandler {
	private static IInventory inv;
	private IInventory prevInventory;
	private static ItemStack lilyStack;
	private static int lilyIndex = -1;
	private static final Item emeralds = Item
			.getByNameOrId("minecraft:emerald");

	public void emptyInventory(EntityPlayer player) {
		int emeraldIndex = -1;
		int emeraldCount = 0;

		prevInventory = new InventoryPlayer(player);
		inv = player.inventory;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (current != null && current.getItem() != null) {
				Item currentItem = current.getItem();
				prevInventory.setInventorySlotContents(i, current);
				if (currentItem == emeralds) {
					emeraldIndex = i;
					emeraldCount += current.stackSize;
				} else {
					inv.setInventorySlotContents(i, null);
				}
			}
		}
		if (emeraldIndex != -1) {
			inv.setInventorySlotContents(emeraldIndex, new ItemStack(emeralds,
					emeraldCount));
		}
	}

	public void refillInventory() {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = prevInventory.getStackInSlot(i);
			if (current != null && current.getItem() != null) {
				Item currentItem = current.getItem();
				prevInventory.setInventorySlotContents(i, current);
				if (currentItem != emeralds) {
					inv.setInventorySlotContents(i,
							prevInventory.getStackInSlot(i));
				}
			}
		}
	}

	public static void removeLilyEggs(EntityPlayer player) {
		inv = player.inventory;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (current != null && current.getItem() != null) {
				Item currentItem = current.getItem();
				if (currentItem.getItemStackDisplayName(current).contains(
						"Lily")) {
					
					lilyStack = current;
					lilyIndex = i;
					inv.setInventorySlotContents(i, null);
				}
			}
		}
	}

	public static void insertLilyEggs(EntityPlayer player) {
		IInventory inventory = player.inventory;
		boolean lilyEggFound = false;
		int emptyIndex = -1;
		
		for (int i = 0; i < inventory.getSizeInventory() && !lilyEggFound; i++) {
			ItemStack current = inventory.getStackInSlot(i);
			if (current != null && current.getItem() != null) {
				Item currentItem = current.getItem();
				if (currentItem.getItemStackDisplayName(current).contains("Lily")) {
					lilyEggFound = true;
				}
			} else {
				emptyIndex = i;
			}
		}
		
		if(!lilyEggFound) {
			if (emptyIndex == -1) {
				emptyIndex = 0;
			}
			inventory.setInventorySlotContents(emptyIndex, new ItemStack(MainRegistry.lilyEgg));
		}
	}
}
