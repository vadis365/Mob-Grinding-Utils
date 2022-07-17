package mob_grinding_utils.inventory.server;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
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
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return item.test(stack);
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return maxItems;
    }

    @Override
    public int getMaxStackSize() {
        return maxItems;
    }
}
