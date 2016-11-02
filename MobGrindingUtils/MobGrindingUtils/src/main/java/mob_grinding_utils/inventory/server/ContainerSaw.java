package mob_grinding_utils.inventory.server;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSaw extends Container {
	private final int numRows = 2;
	TileEntitySaw fan;
	public ContainerSaw(EntityPlayer player, TileEntitySaw tile) {
		InventoryPlayer playerInventory = player.inventory;
		fan = tile;
		int i = (numRows - 4) * 18;
		addSlotToContainer(new SlotRestriction(tile, 0, 18, 18, new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 0), 10));
		addSlotToContainer(new SlotRestriction(tile, 1, 43, 18, new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 1), 10));
		addSlotToContainer(new SlotRestriction(tile, 2, 68, 18, new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 2), 10));
		addSlotToContainer(new SlotRestriction(tile, 3, 93, 18, new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 3), 10));
		addSlotToContainer(new SlotRestriction(tile, 4, 118, 18, new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 4), 10));
		addSlotToContainer(new SlotRestriction(tile, 5, 143, 18, new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 5), 10));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 86 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 144 + i));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotIndex > 5) {
				if (stack1.getItem() == MobGrindingUtils.SAW_UPGRADE && stack1.getItemDamage() == 0)
					if (!mergeItemStack(stack1, 0, 1, false))
						return null;
				if (stack1.getItem() == MobGrindingUtils.SAW_UPGRADE && stack1.getItemDamage() == 1)
					if (!mergeItemStack(stack1, 1, 2, false))
						return null;
				if (stack1.getItem() == MobGrindingUtils.SAW_UPGRADE && stack1.getItemDamage() == 2)
					if (!mergeItemStack(stack1, 2, 3, false))
						return null;
				if (stack1.getItem() == MobGrindingUtils.SAW_UPGRADE && stack1.getItemDamage() == 3)
					if (!mergeItemStack(stack1, 3, 4, false))
						return null;
				if (stack1.getItem() == MobGrindingUtils.SAW_UPGRADE && stack1.getItemDamage() == 4)
					if (!mergeItemStack(stack1, 4, 5, false))
						return null;
				if (stack1.getItem() == MobGrindingUtils.SAW_UPGRADE && stack1.getItemDamage() == 5)
					if (!mergeItemStack(stack1, 5, 6, false))
						return null;
			} else if (!mergeItemStack(stack1, 6, inventorySlots.size(), false))
				return null;
			if (stack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
			if (stack1.stackSize != stack.stackSize)
				slot.onPickupFromSlot(player, stack1);
			else
				return null;
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
			while (stack.stackSize > 0 && (!reverseDirection && slotIndex < endIndex || reverseDirection && slotIndex >= startIndex)) {
				slot = (Slot) this.inventorySlots.get(slotIndex);
				slotstack = slot.getStack();

				if (slotstack != null && slotstack.getItem() == stack.getItem() && stack.getItemDamage() == slotstack.getItemDamage() && ItemStack.areItemStackTagsEqual(stack, slotstack) && slotstack.stackSize < slot.getSlotStackLimit()) {
					int mergedStackSize = stack.stackSize + getSmaller(slotstack.stackSize, slot.getSlotStackLimit());

					if (mergedStackSize <= stack.getMaxStackSize() && mergedStackSize <= slot.getSlotStackLimit()) {
						stack.stackSize = 0;
						slotstack.stackSize = mergedStackSize;
						slot.onSlotChanged();
						merged = true;
					} else if (slotstack.stackSize < stack.getMaxStackSize() && slotstack.stackSize < slot.getSlotStackLimit()) {
						if (slot.getSlotStackLimit() >= stack.getMaxStackSize()) {
							stack.stackSize -= stack.getMaxStackSize() - slotstack.stackSize;
							slotstack.stackSize = stack.getMaxStackSize();
							slot.onSlotChanged();
							merged = true;
						}
						else if (slot.getSlotStackLimit() < stack.getMaxStackSize()) {
							stack.stackSize -= slot.getSlotStackLimit() - slotstack.stackSize;
							slotstack.stackSize = slot.getSlotStackLimit();
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

		if (stack.stackSize > 0) {
			if (reverseDirection)
				slotIndex = endIndex - 1;
			else
				slotIndex = startIndex;

			while (!reverseDirection && slotIndex < endIndex || reverseDirection && slotIndex >= startIndex) {
				slot = (Slot) this.inventorySlots.get(slotIndex);
				slotstack = slot.getStack();
				if (slotstack == null && slot.isItemValid(stack) && slot.getSlotStackLimit() < stack.stackSize) {
					ItemStack copy = stack.copy();
					copy.stackSize = slot.getSlotStackLimit();
					stack.stackSize -= slot.getSlotStackLimit();
					slot.putStack(copy);
					slot.onSlotChanged();
					merged = true;
					break;
				} else if (slotstack == null && slot.isItemValid(stack)) {
					slot.putStack(stack.copy());
					slot.onSlotChanged();
					stack.stackSize = 0;
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