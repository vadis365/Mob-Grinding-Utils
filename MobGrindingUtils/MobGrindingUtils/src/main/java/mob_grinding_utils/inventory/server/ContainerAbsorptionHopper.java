package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;

public class ContainerAbsorptionHopper extends AbstractContainerMenu {

	public int numRows = 2;
	public TileEntityAbsorptionHopper hopper;
	public ContainerAbsorptionHopper(final int windowId, final Inventory playerInventory, FriendlyByteBuf extra) {
		super(ModContainers.ABSORPTION_HOPPER.get(), windowId);
		BlockPos tilePos = extra.readBlockPos();
		BlockEntity tile = playerInventory.player.getCommandSenderWorld().getBlockEntity(tilePos);
		if (!(tile instanceof TileEntityAbsorptionHopper))
			return;
		hopper = (TileEntityAbsorptionHopper) tile;
		int i = (numRows - 4) * 18;
		int j;
		int k;
		addSlot(new SlotRestriction((Container)tile, 0, 134, 72, new ItemStack(ModItems.ABSORPTION_UPGRADE.get(), 1), 6)); // change to hopper upgrade

		for (j = 0; j < numRows; ++j)
			for (k = 0; k < 8; ++k)
				addSlot(new Slot((Container) tile, 1 + k + j * 8, 8 + k * 18, 94 + j * 18));

		for (j = 0; j < 3; ++j)
			for (k = 0; k < 9; ++k)
				addSlot(new Slot(playerInventory, k + j * 9 + 9, 44 + k * 18, 180 + j * 18 + i));

		for (j = 0; j < 9; ++j)
			addSlot(new Slot(playerInventory, j, 44 + j * 18, 238 + i));
	}

	@Override
	public boolean stillValid(@Nonnull Player player) {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(@Nonnull Player player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (slotIndex < numRows * 8 + 1) {
				if (!moveItemStackTo(stack1, numRows * 8 + 1, slots.size(), true))
					return ItemStack.EMPTY;
			} else if (!moveItemStackTo(stack1, 0, numRows * 8 + 1, false))
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
	protected boolean moveItemStackTo(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean merged = false;
		int slotIndex = startIndex;

		if (reverseDirection)
			slotIndex = endIndex - 1;

		Slot slot;
		ItemStack slotstack;

		if (stack.isStackable()) {
			while (stack.getCount() > 0 && (!reverseDirection && slotIndex < endIndex || reverseDirection && slotIndex >= startIndex)) {
				slot = this.slots.get(slotIndex);
				slotstack = slot.getItem();

				if (!slotstack.isEmpty() && slotstack.getItem() == stack.getItem() && stack.getDamageValue() == slotstack.getDamageValue() && ItemStack.tagMatches(stack, slotstack) && slotstack.getCount() < slot.getMaxStackSize()) {
					int mergedStackSize = stack.getCount() + Math.min(slotstack.getCount(), slot.getMaxStackSize());

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
				slot = this.slots.get(slotIndex);
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
}