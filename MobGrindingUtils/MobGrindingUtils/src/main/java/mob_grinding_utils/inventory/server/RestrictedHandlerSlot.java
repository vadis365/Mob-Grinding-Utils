package mob_grinding_utils.inventory.server;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class RestrictedHandlerSlot extends SlotItemHandler {
    Predicate<ItemStack> item;
    int maxItems;

    public RestrictedHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> itemStackPredicate, int max) {
        super(itemHandler, index, xPosition, yPosition);
        item = itemStackPredicate;
        maxItems = max;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return item.test(stack);
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
