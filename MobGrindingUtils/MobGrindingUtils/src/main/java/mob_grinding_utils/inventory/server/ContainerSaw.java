package mob_grinding_utils.inventory.server;

import mob_grinding_utils.ModContainers;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class ContainerSaw extends AbstractContainerMenu {
	private final int numRows = 2;
	public TileEntitySaw saw;
	
	public ContainerSaw(final int windowId, final Inventory playerInventory, FriendlyByteBuf extra) {
		super(ModContainers.SAW.get(), windowId);
		
		BlockPos tilePos = extra.readBlockPos();
		BlockEntity tile = playerInventory.player.getCommandSenderWorld().getBlockEntity(tilePos);
		if (!(tile instanceof TileEntitySaw))
			return;
		saw = (TileEntitySaw) tile;
		int i = (numRows - 4) * 18;

		addSlot(new SlotRestriction((Container) tile, 0, 18, 18, new ItemStack(ModItems.SAW_UPGRADE_SHARPNESS.get(), 1), 10));
		addSlot(new SlotRestriction((Container) tile, 1, 43, 18, new ItemStack(ModItems.SAW_UPGRADE_LOOTING.get(), 1), 10));
		addSlot(new SlotRestriction((Container) tile, 2, 68, 18, new ItemStack(ModItems.SAW_UPGRADE_FIRE.get(), 1), 10));
		addSlot(new SlotRestriction((Container) tile, 3, 93, 18, new ItemStack(ModItems.SAW_UPGRADE_SMITE.get(), 1), 10));
		addSlot(new SlotRestriction((Container) tile, 4, 118, 18, new ItemStack(ModItems.SAW_UPGRADE_ARTHROPOD.get(), 1), 10));
		addSlot(new SlotRestriction((Container) tile, 5, 143, 18, new ItemStack(ModItems.SAW_UPGRADE_BEHEADING.get(), 1), 10));

		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 9; k++)
				addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 86 + j * 18 + i));
		for (int j = 0; j < 9; j++)
			addSlot(new Slot(playerInventory, j, 8 + j * 18, 144 + i));
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
			if (slotIndex > 5) {
				if (stack1.getItem() == ModItems.SAW_UPGRADE_SHARPNESS.get())
					if (!moveItemStackTo(stack1, 0, 1, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_LOOTING.get())
					if (!moveItemStackTo(stack1, 1, 2, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_FIRE.get())
					if (!moveItemStackTo(stack1, 2, 3, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_SMITE.get())
					if (!moveItemStackTo(stack1, 3, 4, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_ARTHROPOD.get())
					if (!moveItemStackTo(stack1, 4, 5, false))
						return ItemStack.EMPTY;
				if (stack1.getItem() == ModItems.SAW_UPGRADE_BEHEADING.get())
					if (!moveItemStackTo(stack1, 5, 6, false))
						return ItemStack.EMPTY;
			} else if (!moveItemStackTo(stack1, 6, slots.size(), false))
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