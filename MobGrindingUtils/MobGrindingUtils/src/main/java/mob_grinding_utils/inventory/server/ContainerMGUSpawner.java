package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;


public class ContainerMGUSpawner extends Container {
    public TileEntityMGUSpawner tile;

    public ContainerMGUSpawner(final int windowId, final PlayerInventory playerInventory, PacketBuffer extra) {
        super(ModContainers.ENTITY_SPAWNER.get(), windowId);
        BlockPos tilePos = extra.readBlockPos();
        TileEntity tile = playerInventory.player.getEntityWorld().getTileEntity(tilePos);
        if (!(tile instanceof TileEntityMGUSpawner))
            return;
        this.tile = (TileEntityMGUSpawner) tile;

        addPlayerSlots(playerInventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
        int originX = 7;
        int originY = 143;

        //Egg
        this.addSlot(new SlotItemHandler(tile.inputSlots, 0, 44, 22 ));
        //Fuel
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 1, 44, 76, ModItems.SOLID_XP_BABY, 64));
        ////Upgrade Area Width
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 2, 8, 112, ModItems.ABSORPTION_UPGRADE, 5));
        //Upgrade Area Height
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 3, 44, 112, ModItems.SAW_UPGRADE_LOOTING, 5));
        //Upgrade Speed
        this.addSlot(new RestrictedHandlerSlot(tile.inputSlots, 4, 80, 112, ModItems.XP_SOLIDIFIER_UPGRADE, 5));

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

			if (index > 4) {
				if (stack1.getItem() instanceof SpawnEggItem)
					if (!this.mergeItemStack(stack1, 0, 1, false))
						return ItemStack.EMPTY;

				if (stack1.getItem() == ModItems.SOLID_XP_BABY)
					if (!this.mergeItemStack(stack1, 1, 2, false))
						return ItemStack.EMPTY;

				if (stack1.getItem() == ModItems.ABSORPTION_UPGRADE)//temp as new items need to be made
					if (!this.mergeItemStack(stack1, 2, 3, false))
						return ItemStack.EMPTY;

				if (stack1.getItem() == ModItems.SAW_UPGRADE_LOOTING)
					if (!this.mergeItemStack(stack1, 3, 4, false))
						return ItemStack.EMPTY;

				if (stack1.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE)
					if (!this.mergeItemStack(stack1, 4, 5, false))
						return ItemStack.EMPTY;

			} else if (!mergeItemStack(stack1, 5, inventorySlots.size(), false))
				return ItemStack.EMPTY;

			if (stack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
	
			if (stack1.getCount() == stack.getCount())
				return ItemStack.EMPTY;
		}
		return stack;
	}
}
