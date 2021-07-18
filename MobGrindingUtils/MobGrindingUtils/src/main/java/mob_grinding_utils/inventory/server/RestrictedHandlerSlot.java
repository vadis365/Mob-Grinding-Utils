package mob_grinding_utils.inventory.server;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RestrictedHandlerSlot extends SlotItemHandler {
    Item item;
    int maxItems;

    public RestrictedHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item targetItem, int max) {
        super(itemHandler, index, xPosition, yPosition);
        item = targetItem;
        maxItems = max;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return stack.getItem() == item;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return maxItems;
    }

    @Override
    public int getSlotStackLimit() {
        return maxItems;
    }
}
