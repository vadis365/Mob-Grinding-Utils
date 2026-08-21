package mob_grinding_utils.inventory.server;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotRestrictSizeOnly extends ResourceHandlerSlot {
    private final int stackLimit;

    public SlotRestrictSizeOnly(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, int max) {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
        this.stackLimit = max;
    }

    @Override
    public int getMaxStackSize() {
        return stackLimit;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return stackLimit;
    }

}
