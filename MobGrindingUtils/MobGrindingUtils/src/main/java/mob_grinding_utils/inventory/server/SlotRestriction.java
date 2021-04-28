package mob_grinding_utils.inventory.server;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotRestriction extends Slot {
	ItemStack item;
	int maxItems;

	public SlotRestriction(IInventory inventory, int slotIndex, int x, int y, ItemStack item, int maxItems) {
		super(inventory, slotIndex, x, y);
		this.item = item;
		this.maxItems = maxItems;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == item.getItem() && stack.getDamage() == item.getDamage();
	}

	@Override
    public int getSlotStackLimit() {
        return maxItems;
    }

	@Override	
    public int getItemStackLimit(ItemStack stack) {
        return this.getSlotStackLimit();
    }
}
