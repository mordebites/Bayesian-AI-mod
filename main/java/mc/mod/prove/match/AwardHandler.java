package mc.mod.prove.match;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AwardHandler {
	private static final String ITEM_NAME = "minecraft:emerald";
	private static final ItemStack EMERALD_STACK = new ItemStack(
			Item.getByNameOrId(ITEM_NAME));

	public static void addItem(EntityPlayer ep, int number) {
		int newAmount = number;
		int currentAmount = 0;

		int index = 0;

		// prende la grandezza dello stack e si segna la posizione se sono già
		// presenti emerald nell'inventario
		IInventory inv = ep.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				if (j.getItem() != null
						&& j.getItem() == EMERALD_STACK.getItem()) {
					currentAmount = j.stackSize;
					index = i;
				}
			}
		}

		// se non sono presenti emerald all'interno dell'inventario aggiungo
		// l'emerald nella prima posizione disponibile (in caso tutto
		// l'inventario
		// dovesse essere pieno rimpiazzo la prima posizione dell'inventario :(
		if (currentAmount == 0) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null) {
					index = i;

					break;
				}
			}
		}

		// sommo la quantita' di vincita della partita
		// alla quantita' presente nell'inventario
		newAmount += currentAmount;

		ItemStack newEmerald = new ItemStack(Item.getByNameOrId(ITEM_NAME),
				newAmount);

		inv.setInventorySlotContents(index, newEmerald);
	}

	public static boolean hasItem(EntityPlayer ep, int number) {
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

	public static void removeItem(EntityPlayer ep, int number) {
		IInventory inv = ep.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				ItemStack j = inv.getStackInSlot(i);
				if (j.getItem() != null
						&& j.getItem() == EMERALD_STACK.getItem()) {
					inv.decrStackSize(i, number);
					break;
				}
			}
		}
	}
}
