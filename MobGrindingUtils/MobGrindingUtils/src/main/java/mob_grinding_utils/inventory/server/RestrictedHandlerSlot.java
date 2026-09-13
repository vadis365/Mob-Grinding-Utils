package mob_grinding_utils.inventory.server;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class RestrictedHandlerSlot extends ResourceHandlerSlot {
    private final Predicate<ItemStack> item;
    private final int maxItems;
    private final int slotIndex;

    public RestrictedHandlerSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> itemStackPredicate, int max) {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
        item = itemStackPredicate;
        maxItems = max;
        slotIndex = index;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return item.test(stack) && getResourceHandler().isValid(slotIndex, ItemResource.of(stack));
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
