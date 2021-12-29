package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class ContainerFan extends AbstractContainerMenu {
	private final int numRows = 2;
	public TileEntityFan fan;

	public ContainerFan(final int windowId, final Inventory playerInventory, FriendlyByteBuf extra) {
		super(ModContainers.FAN.get(), windowId);
		BlockPos tilePos = extra.readBlockPos();
		BlockEntity tile = playerInventory.player.getCommandSenderWorld().getBlockEntity(tilePos);
		if (!(tile instanceof TileEntityFan))
			return;
		fan = (TileEntityFan) tile;

		int i = (numRows - 4) * 18;
		addSlot(new SlotRestriction((Container) tile, 0, 44, 18, new ItemStack(ModItems.FAN_UPGRADE_WIDTH.get(), 1), 3));
		addSlot(new SlotRestriction((Container) tile, 1, 80, 18, new ItemStack(ModItems.FAN_UPGRADE_HEIGHT.get(), 1), 3));
		addSlot(new SlotRestriction((Container) tile, 2, 116, 18, new ItemStack(ModItems.FAN_UPGRADE_SPEED.get(), 1), 10));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 104 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlot(new Slot(playerInventory, j, 8 + j * 18, 162 + i));
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) slots.get(slotIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (slotIndex > 2) {
				if (stack1.getItem() == ModItems.FAN_UPGRADE_WIDTH.get())
					if (!moveItemStackTo(stack1, 0, 1, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.FAN_UPGRADE_HEIGHT.get())
					if (!moveItemStackTo(stack1, 1, 2, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.FAN_UPGRADE_SPEED.get())
					if (!moveItemStackTo(stack1, 2, 3, false))
						return ItemStack.EMPTY;
			} else if (!moveItemStackTo(stack1, 3, slots.size(), false))
				return ItemStack.EMPTY;
			if (stack1.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
			if (stack1.getCount() != stack.getCount())
				slot.onTake(player, stack1);
			else
				return ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean merged = false;
		int slotIndex = startIndex;

		if (reverseDirection)
			slotIndex = endIndex - 1;

		Slot slot;
		ItemStack slotstack;

		if (stack.isStackable()) {
			while (stack.getCount() > 0 && (!reverseDirection && slotIndex < endIndex || reverseDirection && slotIndex >= startIndex)) {
				slot = (Slot) this.slots.get(slotIndex);
				slotstack = slot.getItem();

				if (!slotstack.isEmpty() && slotstack.getItem() == stack.getItem() && stack.getDamageValue() == slotstack.getDamageValue() && ItemStack.tagMatches(stack, slotstack) && slotstack.getCount() < slot.getMaxStackSize()) {
					int mergedStackSize = stack.getCount() + getSmaller(slotstack.getCount(), slot.getMaxStackSize());

					if (mergedStackSize <= stack.getMaxStackSize() && mergedStackSize <= slot.getMaxStackSize()) {
						stack.setCount(0);
						slotstack.setCount(mergedStackSize);
						slot.setChanged();
						merged = true;
					} else if (slotstack.getCount() < stack.getMaxStackSize() && slotstack.getCount() < slot.getMaxStackSize()) {
						if (slot.getMaxStackSize() >= stack.getMaxStackSize()) {
							stack.shrink(stack.getMaxStackSize() - slotstack.getCount());
							slotstack.setCount(stack.getMaxStackSize());
							slot.setChanged();
							merged = true;
						}
						else if (slot.getMaxStackSize() < stack.getMaxStackSize()) {
							stack.shrink(slot.getMaxStackSize() - slotstack.getCount());
							slotstack.setCount(slot.getMaxStackSize());
							slot.setChanged();
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
				slot = (Slot) this.slots.get(slotIndex);
				slotstack = slot.getItem();
				if (slotstack.isEmpty() && slot.mayPlace(stack) && slot.getMaxStackSize() < stack.getCount()) {
					ItemStack copy = stack.copy();
					copy.setCount(slot.getMaxStackSize());
					stack.shrink(slot.getMaxStackSize());
					slot.set(copy);
					slot.setChanged();
					merged = true;
					break;
				} else if (slotstack.isEmpty() && slot.mayPlace(stack)) {
					slot.set(stack.copy());
					slot.setChanged();
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