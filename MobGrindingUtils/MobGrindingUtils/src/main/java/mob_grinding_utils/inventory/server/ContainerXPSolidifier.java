package mob_grinding_utils.inventory.server;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

import java.util.function.Predicate;


public class ContainerXPSolidifier extends AbstractContainerMenu {
    public TileEntityXPSolidifier tile;

    public ContainerXPSolidifier(final int windowId, final Inventory playerInventory, FriendlyByteBuf extra) {
        super(ModContainers.SOLIDIFIER.get(), windowId);
        BlockPos tilePos = extra.readBlockPos();
        BlockEntity tile = playerInventory.player.getCommandSenderWorld().getBlockEntity(tilePos);
        if (!(tile instanceof TileEntityXPSolidifier))
            return;
        this.tile = (TileEntityXPSolidifier) tile;


        addPlayerSlots(playerInventory);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int originX = 7;
        int originY = 103;
        Predicate<ItemStack> mouldPredicate = stack -> {
            for (SolidifyRecipe recipe : MobGrindingUtils.SOLIDIFIER_RECIPES) {
                if(recipe.matches(stack))
                    return true;
            }
            return false;
        };
        //Mould
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 0, 62, 36, mouldPredicate, 1 ));
        //Upgrade
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 1, 26, 72, i -> i.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get(), 9));
        //Output
        this.addSlot(new SlotSolidifierOutput(tile.outputSlot, 0, 130, 36, this));

        //Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = originX + col * 18;
                int y = originY + row * 18;
                int index = (col + row * 9) + 9;
                this.addSlot(new Slot(playerInventory, index, x+1, y+1));
            }
        }

        //Hotbar
        for (int col = 0; col < 9; col++) {
            int x = originX + col * 18;
            int y = originY + 58;
            this.addSlot(new Slot(playerInventory, col, x+1, y+1));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();

            if (index > 2) {
                if (stack1.getItem() == ModItems.SOLID_XP_MOULD_BABY.get())
                    if (!this.moveItemStackTo(stack1, 0, 1, false))
                        return ItemStack.EMPTY;

                if (stack1.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get())
                    if (!this.moveItemStackTo(stack1, 1, 2, false))
                        return ItemStack.EMPTY;

                if (!this.moveItemStackTo(stack1, 2, 3, false))
                    return ItemStack.EMPTY;

            } else if (!moveItemStackTo(stack1, 3, slots.size(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, stack1);

            if (index == 2)
                player.drop(stack1, false);
        }

        return stack;
    }
}
