package mob_grinding_utils.inventory.server;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;


public class ContainerXPSolidifier extends Container {
    public TileEntityXPSolidifier tile;

    public ContainerXPSolidifier(final int windowId, final PlayerInventory playerInventory, PacketBuffer extra) {
        super(ModContainers.SOLIDIFIER.get(), windowId);
        BlockPos tilePos = extra.readBlockPos();
        TileEntity tile = playerInventory.player.getEntityWorld().getTileEntity(tilePos);
        if (!(tile instanceof TileEntityXPSolidifier))
            return;
        this.tile = (TileEntityXPSolidifier) tile;


        addPlayerSlots(playerInventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
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
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index > 2) {
                if (stack1.getItem() == ModItems.SOLID_XP_MOULD_BABY.get())
                    if (!this.mergeItemStack(stack1, 0, 1, false))
                        return ItemStack.EMPTY;

                if (stack1.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get())
                    if (!this.mergeItemStack(stack1, 1, 2, false))
                        return ItemStack.EMPTY;

                if (!this.mergeItemStack(stack1, 2, 3, false))
                    return ItemStack.EMPTY;

            } else if (!mergeItemStack(stack1, 3, inventorySlots.size(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            ItemStack stack2 = slot.onTake(player, stack1);

            if (index == 2)
                player.dropItem(stack2, false);
        }

        return stack;
    }
}
