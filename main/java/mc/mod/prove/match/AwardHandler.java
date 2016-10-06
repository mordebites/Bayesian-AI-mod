package mc.mod.prove.match;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AwardHandler {
	private static final String ITEM_NAME = "minecraft:emerald";
	private static final ItemStack EMERALD_STACK = new ItemStack(
			Item.getByNameOrId(ITEM_NAME));

	public static final int EMERALDS_TO_PLAY = 5;
	public static final int EMERALDS_TO_WIN = 2;
	public static final int EMERALDS_TO_LOSE = 1;

	public static int getEmeraldQuantity(EntityPlayer ep) {
		int itemsNumber = 0;

		// saves stack size and index of the first
		// occurrence of emeralds in the inventory
		IInventory inv = ep.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				if (j.getItem() != null
						&& j.getItem() == EMERALD_STACK.getItem()) {
					itemsNumber += j.stackSize;
				}
			}
		}

		return itemsNumber;
	}

	public static void addEmeralds(EntityPlayer ep, int number) {
		int newAmount = number;
		int currentAmount = 0;

		int index = 0;

		// saves stack size and index of the first
		// occurrence of emeralds in the inventory
		IInventory inv = ep.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				if (j.getItem() != null
						&& j.getItem() == EMERALD_STACK.getItem()) {
					currentAmount = j.stackSize;
					index = i;
					inv.setInventorySlotContents(i, null);
				}
			}
		}

		// if there are no emeralds in the inventory, it adds
		// the emerald into the first available position.
		// if the whole inventory is full, it replaces
		// the first element
		if (currentAmount == 0) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null) {
					index = i;

					break;
				}
			}
		}

		// sums the won quantity to 
		// the quantity already present in the inventory
		newAmount += currentAmount;

		ItemStack newEmerald = new ItemStack(Item.getByNameOrId(ITEM_NAME),
				newAmount);

		inv.setInventorySlotContents(index, newEmerald);
	}

	public static boolean hasEmeralds(EntityPlayer ep, int number) {
		boolean hasIt = false;

		IInventory inv = ep.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				int currentStackSize = j.stackSize;

				if (j.getItem() != null
						&& j.getItem() == EMERALD_STACK.getItem()
						&& currentStackSize >= number) {
					hasIt = true;
				}
			}
		}

		return hasIt;
	}

	public static void removeEmeralds(EntityPlayer ep, int number) {
		IInventory inv = ep.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				if (j.getItem() != null
						&& j.getItem() == EMERALD_STACK.getItem()
						&& j.stackSize >= number) {
					inv.decrStackSize(i, number);
					break;
				}
			}
		}
	}
}
