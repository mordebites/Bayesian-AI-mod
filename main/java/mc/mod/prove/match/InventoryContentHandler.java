package mc.mod.prove.match;

import mc.mod.prove.MainRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryContentHandler {
	private IInventory inv;
	private IInventory prevInventory;
	private int emeraldIndex = -1;
	private int emeraldCount = 0;
	private ItemStack lilyStack;
	
	public void emptyInventory(EntityPlayer player){
		prevInventory = new InventoryPlayer(player);
		inv = player.inventory;
		Item emeralds = Item.getByNameOrId("minecraft:emerald");
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (current != null && current.getItem() != null) {
				Item currentItem = current.getItem();
				prevInventory.setInventorySlotContents(i, current);
				if(currentItem == emeralds) {
					emeraldIndex = i;
					emeraldCount += current.stackSize;
				} else if(currentItem.getUnlocalizedName().compareTo("minecraft:spawn_egg") == 0) {
					lilyStack = current;
				} else {
			
					inv.setInventorySlotContents(i, null);
				}
			}
		}
		if(emeraldIndex != -1) {
			inv.setInventorySlotContents(emeraldIndex, new ItemStack(emeralds, emeraldCount));
		}
	}
	
	public void refillInventory() {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			inv.setInventorySlotContents(i, prevInventory.getStackInSlot(i));
		}
	}
}
