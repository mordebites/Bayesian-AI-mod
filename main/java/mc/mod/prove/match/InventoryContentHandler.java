package mc.mod.prove.match;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryContentHandler {
	private static IInventory inv;
	private IInventory prevInventory;
	private int emeraldIndex = -1;
	private int emeraldCount = 0;
	private static ItemStack lilyStack;
	private static int lilyIndex = -1;
	private static final Item emeralds = Item
			.getByNameOrId("minecraft:emerald");

	public void emptyInventory(EntityPlayer player) {
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
				if (currentItem.getItemStackDisplayName(current).contains("Lily")) {
					lilyStack = current;
					lilyIndex = i;
					inv.setInventorySlotContents(i, null);
				}
			}
		}
	}
	
	public static void insertLilyEggs() {
		if(lilyStack != null) {
			inv.setInventorySlotContents(lilyIndex, lilyStack);
			lilyStack = null;
			lilyIndex = -1;
		}
	}
}
