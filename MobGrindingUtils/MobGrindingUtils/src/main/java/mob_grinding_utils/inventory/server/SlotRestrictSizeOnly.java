package mob_grinding_utils.inventory.server;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotRestrictSizeOnly extends SlotItemHandler {
    private final int stackLimit;

    public SlotRestrictSizeOnly(IItemHandler itemHandler, int index, int xPosition, int yPosition, int max) {
        super(itemHandler, index, xPosition, yPosition);
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
