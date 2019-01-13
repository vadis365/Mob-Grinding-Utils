package mob_grinding_utils.tile;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public abstract class TileEntityInventoryHelper extends TileEntity implements ISidedInventory {

	private NonNullList<ItemStack> inventory;

	public TileEntityInventoryHelper(int invtSize) {
		super(null);//hnnnngh TODO work that shit out too
		inventory = NonNullList.<ItemStack>withSize(invtSize, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}

    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

	@Override
    public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(inventory, index, count);
		if (!itemstack.isEmpty())
			this.markDirty();
		return itemstack;
	}

	@Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        inventory.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
        this.markDirty();
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : inventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);
		this.loadFromNbt(compound);
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		super.write(compound);
		return this.saveToNbt(compound);
	}

	public void loadFromNbt(NBTTagCompound compound) {
		inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, inventory);
	}

	public NBTTagCompound saveToNbt(NBTTagCompound compound) {
		ItemStackHelper.saveAllItems(compound, inventory, false);
		return compound;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {
	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public ITextComponent getName() {
		return null;
		//return (ITextComponent) (this.customName != null ? this.customName : new TextComponentTranslation("container.hopper", new Object[0]));
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	public boolean canInsertItem() {
		return false;
	}
}