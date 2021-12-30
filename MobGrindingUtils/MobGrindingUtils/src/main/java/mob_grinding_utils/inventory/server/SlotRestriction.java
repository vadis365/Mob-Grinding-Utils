package mob_grinding_utils.inventory.server;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotRestriction extends Slot {
	ItemStack item;
	int maxItems;

	public SlotRestriction(Container inventory, int slotIndex, int x, int y, ItemStack item, int maxItems) {
		super(inventory, slotIndex, x, y);
		this.item = item;
		this.maxItems = maxItems;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() == item.getItem() && stack.getDamageValue() == item.getDamageValue();
	}

	@Override
	public int getMaxStackSize() {
		return maxItems;
	}

	@Override
	public int getMaxStackSize(@Nonnull ItemStack stack) {
		return this.getMaxStackSize();
	}
}
