package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;


public class ContainerMGUSpawner extends AbstractContainerMenu {
    public TileEntityMGUSpawner tile;

    public ContainerMGUSpawner(final int windowId, final Inventory playerInventory, FriendlyByteBuf extra) {
        super(ModContainers.ENTITY_SPAWNER.get(), windowId);
        BlockPos tilePos = extra.readBlockPos();
        BlockEntity tile = playerInventory.player.getCommandSenderWorld().getBlockEntity(tilePos);
        if (!(tile instanceof TileEntityMGUSpawner))
            return;
        this.tile = (TileEntityMGUSpawner) tile;

        addPlayerSlots(playerInventory);
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int originX = 7;
        int originY = 143;

        //Egg
        this.addSlot(new SlotRestrictSizeOnly(tile.inputSlots, 0, 44, 22, 1));
        //Fuel
        this.addSlot(new RestrictedHandlerSlot(tile.fuelSlot, 0, 44, 76, i -> i.getItem() == ModItems.SOLID_XP_BABY.get(), 64));
        ////Upgrade Area Width
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 1, 8, 112, i -> i.getItem() == ModItems.SPAWNER_UPGRADE_WIDTH.get(), 5));
        //Upgrade Area Height
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 2, 44, 112, i -> i.getItem() == ModItems.SPAWNER_UPGRADE_HEIGHT.get(), 5));
        //Upgrade Speed
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 3, 80, 112, i -> i.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get(), 5));

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

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();

            if (index > 4) {
                if (stack1.getItem() instanceof SpawnEggItem)
                    if (!this.moveItemStackTo(stack1, 0, 1, false))
                        return ItemStack.EMPTY;

                if (stack1.getItem() == ModItems.SOLID_XP_BABY.get())
                    if (!this.moveItemStackTo(stack1, 1, 2, false))
                        return ItemStack.EMPTY;

                if (stack1.getItem() == ModItems.SPAWNER_UPGRADE_WIDTH.get())//temp as new items need to be made
                    if (!this.moveItemStackTo(stack1, 2, 3, false))
                        return ItemStack.EMPTY;

                if (stack1.getItem() == ModItems.SPAWNER_UPGRADE_HEIGHT.get())
                    if (!this.moveItemStackTo(stack1, 3, 4, false))
                        return ItemStack.EMPTY;

                if (stack1.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get())
                    if (!this.moveItemStackTo(stack1, 4, 5, false))
                        return ItemStack.EMPTY;

            } else if (!moveItemStackTo(stack1, 5, slots.size(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;
        }
        return stack;
    }
}
