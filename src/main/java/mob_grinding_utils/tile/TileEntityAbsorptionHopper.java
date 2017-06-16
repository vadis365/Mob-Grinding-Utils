package mob_grinding_utils.tile;

import java.util.List;

import javax.annotation.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mob_grinding_utils.ItemBlockRegister;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.inventory.server.InventoryWrapperAH;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;


public class TileEntityAbsorptionHopper extends TileEntityInventoryHelper implements ITickable {
    public FluidTankTile tank;
    private IItemHandler itemHandler;
    private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

	public TileEntityAbsorptionHopper() {
		super(17);
        this.tank = new FluidTankTile(null, Fluid.BUCKET_VOLUME * 16);
        this.tank.setTileEntity(this);
	}

	public enum EnumStatus implements IStringSerializable {
		STATUS_NONE("none"),
		STATUS_OUTPUT_ITEM("item"),
		STATUS_OUTPUT_FLUID("fluid");

		private final String name;

		EnumStatus(String name) {
			this.name = name;
		}

		@Override
		@MethodsReturnNonnullByDefault
		public String getName() {
			return name;
		}
	}

	public EnumStatus status[] = new EnumStatus[] { EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE };

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		EnumStatus[] old = new EnumStatus[] { status[0], status[1], status[2], status[3], status[4], status[5] };
		super.onDataPacket(net, packet);
		readFromNBT(packet.getNbtCompound());
		tank.onContentsChanged();
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (old[facing.ordinal()] != status[facing.ordinal()]) {
				worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
				return;
			}
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 0, tag);
	}

	@Override
	@MethodsReturnNonnullByDefault
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		status[0] = EnumStatus.values()[tagCompound.getByte("down")];
		status[1] = EnumStatus.values()[tagCompound.getByte("up")];
		status[2] = EnumStatus.values()[tagCompound.getByte("north")];
		status[3] = EnumStatus.values()[tagCompound.getByte("south")];
		status[4] = EnumStatus.values()[tagCompound.getByte("west")];
		status[5] = EnumStatus.values()[tagCompound.getByte("east")];
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setByte("down", (byte) status[0].ordinal());
		tagCompound.setByte("up", (byte) status[1].ordinal());
		tagCompound.setByte("north", (byte) status[2].ordinal());
		tagCompound.setByte("south", (byte) status[3].ordinal());
		tagCompound.setByte("west", (byte) status[4].ordinal());
		tagCompound.setByte("east", (byte) status[5].ordinal());
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	public EnumStatus getSideStatus(EnumFacing side) {
		return status[side.ordinal()];
	}

	public void toggleMode(EnumFacing side) {
		switch (status[side.ordinal()]) {
		case STATUS_NONE:
			status[side.ordinal()] = EnumStatus.STATUS_OUTPUT_ITEM;
			break;
		case STATUS_OUTPUT_ITEM:
			status[side.ordinal()] = EnumStatus.STATUS_OUTPUT_FLUID;
			break;
		case STATUS_OUTPUT_FLUID:
			status[side.ordinal()] = EnumStatus.STATUS_NONE;
			break;
		}
		markDirty();
	}

	@Override
	public void update() {
		if (worldObj.isRemote)
			return;

		for (EnumFacing facing : EnumFacing.VALUES) {
			if (status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
				TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
				
				if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
					IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
					if (worldObj.getTotalWorldTime() % 8 == 0) {
						for (int i = 0; i < this.getSizeInventory(); ++i) {
							if (getStackInSlot(i) != null && i != 0) {
								ItemStack stack = getStackInSlot(i).copy();
								stack.stackSize = 1;
								ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
								if (stack1 == null || stack1.stackSize == 0) {
									ItemHandlerHelper.insertItem(handler, this.decrStackSize(i, 1), false);
									this.markDirty();
								}
							}
						}
					}
				}
				else if (tile != null && tile instanceof IInventory) {
					IInventory iinventory = (IInventory) tile;
					if (isInventoryFull(iinventory, facing)) {
						break;
					} else if (worldObj.getTotalWorldTime() % 8 == 0) {
						for (int i = 0; i < this.getSizeInventory(); ++i) {
							if (getStackInSlot(i) != null && i != 0) {
								ItemStack stack = getStackInSlot(i).copy();
								ItemStack stack1 = putStackInInventoryAllSlots(iinventory, decrStackSize(i, 1), facing.getOpposite());
								if (stack1 == null || stack1.stackSize == 0)
									iinventory.markDirty();
								else
									setInventorySlotContents(i, stack);
							}
						}
					}
				}
			}

			if (status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
				TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
				if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
					IFluidHandler recepticle = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
					IFluidTankProperties[] tankProperties = recepticle.getTankProperties();
					for (IFluidTankProperties properties : tankProperties) {
						if (properties.canFill() && properties.getCapacity() > 0) {
							FluidStack contents = properties.getContents();
							if (tank.getFluid() != null) {
								if (contents == null || contents.amount <= properties.getCapacity() - 100 && contents.containsFluid(new FluidStack(tank.getFluid(), 0))) {
									recepticle.fill(tank.drain(new FluidStack(tank.getFluid(), 100), true), true);
									markDirty();
								}
							}
						}
					}
				}
			}
		}

		if (worldObj.getTotalWorldTime() % 3 == 0) {
			if(!isInventoryFull(this, null))
				captureDroppedItems();
			if(tank.getFluid() == null || tank.getFluid().containsFluid(new FluidStack(FluidRegistry.getFluid("xpjuice"), 0)))
				captureDroppedXP();
		}
	}

	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	public boolean captureDroppedItems() {
		for (EntityItem entityitem : getCaptureItems(getWorld(), getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D))
			if (putDropInInventoryAllSlots(this, entityitem))
				return true;
				return false;
    }

    public List<EntityItem> getCaptureItems(World world, double x, double y, double z) {
		int modifier = 0;
		if(hasUpgrade())
		 modifier = inventory[0].stackSize;
        return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - 3.5D - modifier, y - 3.5D - modifier, z - 3.5D - modifier, x + 3.5D + modifier, y + 3.5D + modifier, z + 3.5D + modifier), EntitySelectors.IS_ALIVE);
    }
    
	public boolean captureDroppedXP() {
		for (EntityXPOrb entity : getCaptureXP(getWorld(), getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D)) {
			int xpAmount = entity.getXpValue();
			if (tank.getFluidAmount() < tank.getCapacity() - xpAmount * 20) {
				tank.fill(new FluidStack(FluidRegistry.getFluid("xpjuice"), xpAmount * 20), true);
				entity.setDead();
			}
			return true;
		}
		return false;
	}

	public List<EntityXPOrb> getCaptureXP(World world, double x, double y, double z) {
		int modifier = 0;
		if(hasUpgrade())
		 modifier = inventory[0].stackSize;
        return world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(x - 3.5D - modifier, y - 3.5D - modifier, z - 3.5D - modifier, x + 3.5D + modifier, y + 3.5D + modifier, z + 3.5D + modifier), EntitySelectors.IS_ALIVE);
    }

	private boolean hasUpgrade() {
		return inventory[0] != null && inventory[0].getItem() == ItemBlockRegister.ABSORPTION_UPGRADE;
	}

    @Override
    @Nullable
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

	@Override
	@ParametersAreNonnullByDefault
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot != 0;
    }
	
	@Override
	@ParametersAreNonnullByDefault
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(slot, stack);
	}
	
	@Override
	@ParametersAreNonnullByDefault
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return slot != 0;
	}

	private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int k : aint) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
					return false;
			}
		} else {
			int i = inventoryIn.getSizeInventory();

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = inventoryIn.getStackInSlot(j);

				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
					return false;
			}
		}

		return true;
	}

	public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, @Nullable EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory && side != null && !(inventoryIn instanceof TileEntityAbsorptionHopper) && inventoryIn.isItemValidForSlot(0, stack.copy())) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int k = 0; k < aint.length && stack != null && stack.stackSize > 0; ++k)
				stack = insertStack(inventoryIn, stack, aint[k], side);
		} else {
			int i = inventoryIn.getSizeInventory();

			for (int j = 0; j < i && stack != null && stack.stackSize > 0; ++j)
				stack = insertStack(inventoryIn, stack, j, side);
		}

		if (stack != null && stack.stackSize == 0)
			stack = null;

		return stack;
	}
	
	public static boolean putDropInInventoryAllSlots(IInventory inventoryIn, EntityItem itemIn) {
		boolean flag = false;

		if (itemIn == null || inventoryIn instanceof TileEntityAbsorptionHopper && inventoryIn.isItemValidForSlot(0, itemIn.getEntityItem().copy())) {
			return false;
		} else {
			ItemStack itemstack = itemIn.getEntityItem().copy();
			ItemStack itemstack1 = putStackInInventoryAllSlots(inventoryIn, itemstack, null);

			if (itemstack1 != null && itemstack1.stackSize != 0) {
				itemIn.setEntityItemStack(itemstack1);
			} else {
				flag = true;
				itemIn.setDead();
			}
			return flag;
		}
	}

    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        return inventoryIn.isItemValidForSlot(index, stack) && (
					!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory) inventoryIn)
						.canInsertItem(index, stack, side));
    }

	private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		ItemStack itemstack = inventoryIn.getStackInSlot(index);

		if (canInsertItemInSlot(inventoryIn, stack, index, side)) {
			boolean flag = false;

			if (itemstack == null) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
				if (max >= stack.stackSize) {
					inventoryIn.setInventorySlotContents(index, stack);
					stack = null;
				} else
					inventoryIn.setInventorySlotContents(index, stack.splitStack(max));

				flag = true;

			} else if (canCombine(itemstack, stack)) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
				if (max > itemstack.stackSize) {
					int i = max - itemstack.stackSize;
					int j = Math.min(stack.stackSize, i);
					stack.stackSize -= j;
					itemstack.stackSize += j;
					flag = j > 0;
				}
			}
		}

		return stack;
	}
  
    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (stack1.getMetadata() == stack2.getMetadata()
					&& (stack1.stackSize <= stack1.getMaxStackSize() && ItemStack
					.areItemStackTagsEqual(stack1, stack2)));
    }

// FLUID & INVENTORY CAPABILITIES STUFF

	protected IItemHandler createUnSidedHandler() {
		return new InventoryWrapperAH(this);
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluid().amount / (float) tank.getCapacity() * scale) : 0;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@ParametersAreNonnullByDefault
	@MethodsReturnNonnullByDefault
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler);
		return super.getCapability(capability, facing);
	}
}

