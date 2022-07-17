package mob_grinding_utils.inventory.server;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotSolidifierOutput extends SlotItemHandler {
    @SuppressWarnings("unused")
    private AbstractContainerMenu container;

    public SlotSolidifierOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition, AbstractContainerMenu container) {
        super(itemHandler, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
        // keeping for possible stuffs
        super.onTake(player, stack);
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
