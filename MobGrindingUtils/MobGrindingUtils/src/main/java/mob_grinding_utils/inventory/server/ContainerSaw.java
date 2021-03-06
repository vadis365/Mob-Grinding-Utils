package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSaw extends Container {
	private final int numRows = 2;
	TileEntitySaw fan;
	public ContainerSaw(PlayerEntity player, TileEntitySaw tile) {
		//TODO :P
		super(); //todo
		PlayerInventory playerInventory = player.inventory;
		fan = tile;
		int i = (numRows - 4) * 18;

		addSlot(new SlotRestriction(tile, 0, 18, 18, new ItemStack(ModItems.SAW_UPGRADE_SHARPNESS, 1), 10));
		addSlot(new SlotRestriction(tile, 1, 43, 18, new ItemStack(ModItems.SAW_UPGRADE_LOOTING, 1), 10));
		addSlot(new SlotRestriction(tile, 2, 68, 18, new ItemStack(ModItems.SAW_UPGRADE_FIRE, 1), 10));
		addSlot(new SlotRestriction(tile, 3, 93, 18, new ItemStack(ModItems.SAW_UPGRADE_SMITE, 1), 10));
		addSlot(new SlotRestriction(tile, 4, 118, 18, new ItemStack(ModItems.SAW_UPGRADE_ARTHROPOD, 1), 10));
		addSlot(new SlotRestriction(tile, 5, 143, 18, new ItemStack(ModItems.SAW_UPGRADE_BEHEADING, 1), 10));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 86 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlot(new Slot(playerInventory, j, 8 + j * 18, 144 + i));
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
			if (slotIndex > 5) {
				if (stack1.getItem() == ModItems.SAW_UPGRADE_SHARPNESS)
					if (!mergeItemStack(stack1, 0, 1, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_LOOTING)
					if (!mergeItemStack(stack1, 1, 2, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_FIRE)
					if (!mergeItemStack(stack1, 2, 3, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_SMITE)
					if (!mergeItemStack(stack1, 3, 4, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_ARTHROPOD)
					if (!mergeItemStack(stack1, 4, 5, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_BEHEADING)
					if (!mergeItemStack(stack1, 5, 6, false))
						return ItemStack.EMPTY;
			} else if (!mergeItemStack(stack1, 6, inventorySlots.size(), false))
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