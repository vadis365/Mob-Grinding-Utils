package mob_grinding_utils.inventory.server;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class InventoryWrapperAH implements IItemHandlerModifiable {
	private final Container inv;

	public InventoryWrapperAH(Container inv) {
		this.inv = inv;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		InventoryWrapperAH that = (InventoryWrapperAH) o;

		return getInv().equals(that.getInv());

	}

	@Override
	public int hashCode() {
		return getInv().hashCode();
	}

	@Override
	public int getSlots() {
		return getInv().getContainerSize();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return getInv().getItem(slot);
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		ItemStack stackInSlot = getInv().getItem(slot);

		int m;
		if (!stackInSlot.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
				return stack;

			if (!getInv().canPlaceItem(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

			if (stack.getCount() <= m) {
				if (!simulate) {
					ItemStack copy = stack.copy();
					copy.grow(stackInSlot.getCount());
					getInv().setItem(slot, copy);
					getInv().setChanged();
				}

				return ItemStack.EMPTY;
			} else {
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate) {
					ItemStack copy = stack.split(m);
					copy.grow(stackInSlot.getCount());
					getInv().setItem(slot, copy);
					getInv().setChanged();
				} else {
					stack.shrink(m);
				}
				return stack;
			}
		} else {
			if (!getInv().canPlaceItem(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
			if (m < stack.getCount()) {
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate) {
					getInv().setItem(slot, stack.split(m));
					getInv().setChanged();
				} else {
					stack.shrink(m);
				}
				return stack;
			} else {
				if (!simulate) {
					getInv().setItem(slot, stack);
					getInv().setChanged();
				}
				return ItemStack.EMPTY;
			}
		}

	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0)
			return ItemStack.EMPTY;

		ItemStack stackInSlot = getInv().getItem(slot);

		if (stackInSlot.isEmpty())
			return ItemStack.EMPTY;

		if (slot == 0)
			return ItemStack.EMPTY;

		if (simulate) {
			if (stackInSlot.getCount() < amount) {
				return stackInSlot.copy();
			} else {
				ItemStack copy = stackInSlot.copy();
				copy.setCount(amount);
				return copy;
			}
		} else {
			int m = Math.min(stackInSlot.getCount(), amount);

			ItemStack decrStackSize = getInv().removeItem(slot, m);
			getInv().setChanged();
			return decrStackSize;
		}
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		getInv().setItem(slot, stack);
	}

	@Override
	public int getSlotLimit(int slot) {
		return getInv().getMaxStackSize();
	}

	public Container getInv() {
		return inv;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return slot != 0;
	}
}