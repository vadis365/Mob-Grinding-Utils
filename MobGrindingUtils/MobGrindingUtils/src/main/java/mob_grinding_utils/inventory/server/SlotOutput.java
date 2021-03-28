package mob_grinding_utils.inventory.server;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOutput extends SlotItemHandler {
    private Container container;

    public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition, Container container) {
        super(itemHandler, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
    	// keeping for possible stuffs
        return super.onTake(player, stack);
    }
}
