package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFan extends Container {
	private final int numRows = 2;
	TileEntityFan fan;
    //TODO fix this to work ;)
	public ContainerFan(int windowId, PlayerEntity player, TileEntityFan tile) {
		PlayerInventory playerInventory = player.inventory;
		fan = tile;
		int i = (numRows - 4) * 18;
		addSlot(new SlotRestriction(tile, 0, 44, 18, new ItemStack(ModItems.FAN_UPGRADE_WIDTH, 1), 3));
		addSlot(new SlotRestriction(tile, 1, 80, 18, new ItemStack(ModItems.FAN_UPGRADE_HEIGHT, 1), 3));
		addSlot(new SlotRestriction(tile, 2, 116, 18, new ItemStack(ModItems.FAN_UPGRADE_SPEED, 1), 10));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 104 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlot(new Slot(playerInventory, j, 8 + j * 18, 162 + i));
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 2) {
				if (stack1.getItem() == ModItems.FAN_UPGRADE_WIDTH)
					if (!mergeItemStack(stack1, 0, 1, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.FAN_UPGRADE_HEIGHT)
					if (!mergeItemStack(stack1, 1, 2, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.FAN_UPGRADE_SPEED)
					if (!mergeItemStack(stack1, 2, 3, false))
						return ItemStack.EMPTY;
			} else if (!mergeItemStack(stack1, 3, inventorySlots.size(), false))
				return ItemStack.EMPTY;
			if (stack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			if (stack1.getCount() != stack.getCount())
				slot.onTake(player, stack1);
			else
				return ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean merged = false;
		int slotIndex = startIndex;

		if (reverseDirection)
			slotIndex = endIndex - 1;

		Slot slot;
		ItemStack slotstack;

		if (stack.isStackable()) {
			while (stack.getCount() > 0 && (!reverseDirection && slotIndex < endIndex || reverseDirection && slotIndex >= startIndex)) {
				slot = (Slot) this.inventorySlots.get(slotIndex);
				slotstack = slot.getStack();

				if (!slotstack.isEmpty() && slotstack.getItem() == stack.getItem() && stack.getDamage() == slotstack.getDamage() && ItemStack.areItemStackTagsEqual(stack, slotstack) && slotstack.getCount() < slot.getSlotStackLimit()) {
					int mergedStackSize = stack.getCount() + getSmaller(slotstack.getCount(), slot.getSlotStackLimit());

					if (mergedStackSize <= stack.getMaxStackSize() && mergedStackSize <= slot.getSlotStackLimit()) {
						stack.setCount(0);
						slotstack.setCount(mergedStackSize);
						slot.onSlotChanged();
						merged = true;
					} else if (slotstack.getCount() < stack.getMaxStackSize() && slotstack.getCount() < slot.getSlotStackLimit()) {
						if (slot.getSlotStackLimit() >= stack.getMaxStackSize()) {
							stack.shrink(stack.getMaxStackSize() - slotstack.getCount());
							slotstack.setCount(stack.getMaxStackSize());
							slot.onSlotChanged();
							merged = true;
						}
						else if (slot.getSlotStackLimit() < stack.getMaxStackSize()) {
							stack.shrink(slot.getSlotStackLimit() - slotstack.getCount());
							slotstack.setCount(slot.getSlotStackLimit());
							slot.onSlotChanged();
							merged = true;
						}
					}
				}

				if (reverseDirection)
					--slotIndex;
				else
					++slotIndex;
			}
		}

		if (stack.getCount() > 0) {
			if (reverseDirection)
				slotIndex = endIndex - 1;
			else
				slotIndex = startIndex;

			while (!reverseDirection && slotIndex < endIndex || reverseDirection && slotIndex >= startIndex) {
				slot = (Slot) this.inventorySlots.get(slotIndex);
				slotstack = slot.getStack();
				if (slotstack.isEmpty() && slot.isItemValid(stack) && slot.getSlotStackLimit() < stack.getCount()) {
					ItemStack copy = stack.copy();
					copy.setCount(slot.getSlotStackLimit());
					stack.shrink(slot.getSlotStackLimit());
					slot.putStack(copy);
					slot.onSlotChanged();
					merged = true;
					break;
				} else if (slotstack.isEmpty() && slot.isItemValid(stack)) {
					slot.putStack(stack.copy());
					slot.onSlotChanged();
					stack.setCount(0);
					merged = true;
					break;
				}

				if (reverseDirection)
					--slotIndex;
				else
					++slotIndex;
			}
		}

		return merged;
	}

	protected int getSmaller(int stackSize1, int stackSize2) {
		if (stackSize1 < stackSize2)
			return stackSize1;
		else
			return stackSize2;
	}

}