package mob_grinding_utils.tile;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.inventory.server.InventoryWrapperAH;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;


public class TileEntityAbsorptionHopper extends TileEntityInventoryHelper implements ITickableTileEntity, INamedContainerProvider {
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME *  16);
    private final LazyOptional<IFluidHandler> tank_holder = LazyOptional.of(() -> tank);
	private final IItemHandler itemHandler;
	private LazyOptional<IItemHandler> itemholder = LazyOptional.empty();
    private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    public int prevTankAmount;
	public TileEntityAbsorptionHopper() {
		super(ModBlocks.ABSORPTION_HOPPER.getTileEntityType(), 17);
		itemHandler = createUnSidedHandler();
		itemholder = LazyOptional.of(() -> itemHandler);
	}

	public enum EnumStatus implements IStringSerializable {
		STATUS_NONE("none"),
		STATUS_OUTPUT_ITEM("item"),
		STATUS_OUTPUT_FLUID("fluid");

		private final String name;

		EnumStatus(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getString() {
			return name;
		}
	}

	public EnumStatus[] status = new EnumStatus[] { EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE };
	public boolean showRenderBox;
	public int offsetX, offsetY, offsetZ;

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		EnumStatus[] old = new EnumStatus[] { status[0], status[1], status[2], status[3], status[4], status[5] };
		super.onDataPacket(net, packet);
		read(getBlockState(), packet.getNbtCompound());
		for (Direction facing : Direction.values()) {
			if (old[facing.ordinal()] != status[facing.ordinal()]) {
				getWorld().markBlockRangeForRenderUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()));
				return;
			}
		}
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(getPos(), 0, nbt);
	}

		@Override
	    public CompoundNBT getUpdateTag() {
			CompoundNBT nbt = new CompoundNBT();
	        return write(nbt);
	    }

	@Override
	public void read(BlockState state, CompoundNBT tagCompound) {
		super.read(state, tagCompound);
		status[0] = EnumStatus.values()[tagCompound.getByte("down")];
		status[1] = EnumStatus.values()[tagCompound.getByte("up")];
		status[2] = EnumStatus.values()[tagCompound.getByte("north")];
		status[3] = EnumStatus.values()[tagCompound.getByte("south")];
		status[4] = EnumStatus.values()[tagCompound.getByte("west")];
		status[5] = EnumStatus.values()[tagCompound.getByte("east")];
		showRenderBox = tagCompound.getBoolean("showRenderBox");
		offsetX = tagCompound.getInt("offsetX");
		offsetY = tagCompound.getInt("offsetY");
		offsetZ = tagCompound.getInt("offsetZ");
		tank.readFromNBT(tagCompound);
	}

	@Override
	public CompoundNBT write(CompoundNBT tagCompound) {
		super.write(tagCompound);
		tagCompound.putByte("down", (byte) status[0].ordinal());
		tagCompound.putByte("up", (byte) status[1].ordinal());
		tagCompound.putByte("north", (byte) status[2].ordinal());
		tagCompound.putByte("south", (byte) status[3].ordinal());
		tagCompound.putByte("west", (byte) status[4].ordinal());
		tagCompound.putByte("east", (byte) status[5].ordinal());
		tagCompound.putBoolean("showRenderBox", showRenderBox);
		tagCompound.putInt("offsetX", offsetX);
		tagCompound.putInt("offsetY", offsetY);
		tagCompound.putInt("offsetZ", offsetZ);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	public EnumStatus getSideStatus(Direction side) {
		return status[side.ordinal()];
	}

	public void toggleMode(Direction side) {
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

	public void toggleRenderBox() {
		showRenderBox = !showRenderBox;
		markDirty();
	}

	public void toggleOffset(int direction) {
		switch (direction) {
		case 7:
			if (getoffsetY() >= -3 - getModifierAmount())
				offsetY = getoffsetY() - 1;
			break;
		case 8:
			if (getoffsetY() <= 3 + getModifierAmount())
				offsetY = getoffsetY() + 1;
			break;
		case 9:
			if (getoffsetZ() >= -3 - getModifierAmount())
				offsetZ = getoffsetZ() - 1;
			break;
		case 10:
			if (getoffsetZ() <= 3 + getModifierAmount())
				offsetZ = getoffsetZ() + 1;
			break;
		case 11:
			if (getoffsetX() >= -3 - getModifierAmount())
				offsetX = getoffsetX() - 1;
			break;
		case 12:
			if (getoffsetX() <= 3 + getModifierAmount())
				offsetX = getoffsetX() + 1;
			break;
		}
		markDirty();
	}

	public void updateBlock() {
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}

	@Override
	public void tick() {
		if (world == null || world.isRemote)
			return;
		prevTankAmount = tank.getFluidAmount();
		for (Direction facing : Direction.values()) {
			if (status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
				TileEntity tile = world.getTileEntity(pos.offset(facing));
				if (tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).isPresent()) {
					LazyOptional<IItemHandler> tileOptional = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
					tileOptional.ifPresent((handler) -> {
						if (world.getGameTime() % 8 == 0) {
							for (int i = 0; i < this.getSizeInventory(); ++i) {
								if (!getStackInSlot(i).isEmpty() && i != 0) {
									ItemStack stack = getStackInSlot(i).copy();
									stack.setCount(1);
									ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
									if (stack1.isEmpty()) {
										ItemHandlerHelper.insertItem(handler, this.decrStackSize(i, 1), false);
										this.markDirty();
									}
								}
							}
						}
					});
				}
				else if (tile instanceof IInventory) {
					IInventory iinventory = (IInventory) tile;
					if (isInventoryFull(iinventory, facing))
						break;
					else if (world.getGameTime() % 8 == 0) {
						for (int i = 0; i < this.getSizeInventory(); ++i) {
							if (!getStackInSlot(i).isEmpty() && i != 0) {
								ItemStack stack = getStackInSlot(i).copy();
								ItemStack stack1 = putStackInInventoryAllSlots(iinventory, decrStackSize(i, 1), facing.getOpposite());
								if (stack1.isEmpty() || stack1.getCount() == 0)
									iinventory.markDirty();
								else
									setInventorySlotContents(i, stack);
							}
						}
					}
				}
			}

			if (status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
				TileEntity tile = world.getTileEntity(pos.offset(facing));
				if (tile != null) {
					LazyOptional<IFluidHandler> tileOptional = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
					tileOptional.ifPresent((receptacle) -> {
						int tanks = receptacle.getTanks();
						for (int x = 0; x < tanks; x++) {
							if (receptacle.getTankCapacity(x) > 0) {
								FluidStack contents = receptacle.getFluidInTank(x);
								if (!tank.getFluid().isEmpty()) {
									if (contents.isEmpty() || contents.getAmount() <= receptacle.getTankCapacity(x) - 100 && contents.containsFluid(new FluidStack(tank.getFluid(), 1))) {
										receptacle.fill(tank.drain(new FluidStack(tank.getFluid(), 100), FluidAction.EXECUTE), FluidAction.EXECUTE);
										markDirty();
									}
								}
							}
						}
					});
				}
			}
		}

		if (world.getGameTime() % 3 == 0 && !world.isBlockPowered(pos)) {
			if(!isInventoryFull(this, null))
				captureDroppedItems();
			if(tank.getFluid().isEmpty() || tank.getFluid().containsFluid(new FluidStack(ModBlocks.FLUID_XP.get(), 1)))
				captureDroppedXP();
		}
		
		if(prevTankAmount != tank.getFluidAmount())
			updateBlock();
	}

	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(getItems(), index, count);
	}

	public boolean captureDroppedItems() {
		for (ItemEntity entityitem : getCaptureItems())
			if (putDropInInventoryAllSlots(this, entityitem))
                    return true;
        return false;
    }

    public List<ItemEntity> getCaptureItems() {
    	return getWorld().<ItemEntity>getEntitiesWithinAABB(ItemEntity.class, getAABBWithModifiers(), EntityPredicates.IS_ALIVE);
    }

	public boolean captureDroppedXP() {
		for (ExperienceOrbEntity entity : getCaptureXP()) {
			int xpAmount = entity.getXpValue();
			if (tank.getFluidAmount() < tank.getCapacity() - xpAmount * 20) {
				tank.fill(new FluidStack(ModBlocks.FLUID_XP.get(), xpAmount * 20), FluidAction.EXECUTE);
				entity.xpValue = 0;
				entity.remove();
			}
			return true;
		}
		return false;
	}

	public List<ExperienceOrbEntity> getCaptureXP() {
		return getWorld().<ExperienceOrbEntity>getEntitiesWithinAABB(ExperienceOrbEntity.class, getAABBWithModifiers(), EntityPredicates.IS_ALIVE);
    }

	public AxisAlignedBB getAABBWithModifiers() {
		double x = getPos().getX() + 0.5D;
		double y = getPos().getY() + 0.5D;
		double z = getPos().getZ() + 0.5D;
		return new AxisAlignedBB(x - 3.5D - getModifierAmount(), y - 3.5D - getModifierAmount(), z - 3.5D - getModifierAmount(), x + 3.5D + getModifierAmount(), y + 3.5D + getModifierAmount(), z + 3.5D + getModifierAmount()).offset(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getAABBForRender() {
		return new AxisAlignedBB(- 3D - getModifierAmount(), - 3D - getModifierAmount(), - 3D - getModifierAmount(), 4D + getModifierAmount(), 4D + getModifierAmount(), 4D + getModifierAmount()).offset(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return getAABBWithModifiers();
	}

	public int getoffsetX() {
		return Math.max(- 4 - getModifierAmount(), Math.min(offsetX, 4 + getModifierAmount()));
	}

	public int getoffsetY() {
		return Math.max(- 4 - getModifierAmount(), Math.min(offsetY, 4 + getModifierAmount()));
	}

	public int getoffsetZ() {
		return Math.max(- 4 - getModifierAmount(), Math.min(offsetZ, 4 + getModifierAmount()));
	}

	private boolean hasUpgrade() {
		return !getItems().get(0).isEmpty() && getItems().get(0).getItem() == ModItems.ABSORPTION_UPGRADE.get();
	}

	public int getModifierAmount() {
		return hasUpgrade() ? getItems().get(0).getCount() : 0;
	}

    @Nonnull
	@Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(getItems(), index);
    }

	@Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot != 0;
    }
	
	@Nonnull
	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, Direction direction) {
		return isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, Direction direction) {
		return slot != 0;
	}

	private boolean isInventoryFull(IInventory inventoryIn, Direction side) {
		if (inventoryIn instanceof ISidedInventory) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int k : aint) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

				if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
					return false;
			}
		} else {
			int i = inventoryIn.getSizeInventory();

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = inventoryIn.getStackInSlot(j);

				if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
					return false;
			}
		}

		return true;
	}

    public static ItemStack putStackInInventoryAllSlots(IInventory inventory, ItemStack stack, @Nullable Direction facing) {
        if (inventory instanceof ISidedInventory && facing != null && !(inventory instanceof TileEntityAbsorptionHopper) && inventory.isItemValidForSlot(0, stack.copy())) {
            ISidedInventory isidedinventory = (ISidedInventory)inventory;
            int[] aint = isidedinventory.getSlotsForFace(facing);
            for (int k = 0; k < aint.length && !stack.isEmpty(); ++k)
            	stack = insertStack(inventory, stack, aint[k], facing);
        } else {
            int i = inventory.getSizeInventory();
            for (int j = 0; j < i && !stack.isEmpty(); ++j)
            	stack = insertStack(inventory, stack, j, facing);
        }
        return stack;
    }

	public static boolean putDropInInventoryAllSlots(IInventory inventoryIn, ItemEntity itemIn) {
		boolean flag = false;

		if (itemIn == null || inventoryIn instanceof TileEntityAbsorptionHopper && inventoryIn.isItemValidForSlot(0, itemIn.getItem().copy())) {
			return false;
		} else {
			ItemStack itemstack = itemIn.getItem().copy();
			ItemStack itemstack1 = putStackInInventoryAllSlots(inventoryIn, itemstack, (Direction) null);

			if (!itemstack1.isEmpty()) {
				itemIn.setItem(itemstack1);
			} else {
				flag = true;
				itemIn.remove();
			}
			return flag;
		}
	}

    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, Direction side) {
        return inventoryIn.isItemValidForSlot(index, stack) && (!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory) inventoryIn).canInsertItem(index, stack, side));
    }

	private static ItemStack insertStack( IInventory inventory, ItemStack stack, int index, Direction side) {
        ItemStack itemstack = inventory.getStackInSlot(index);
        if (canInsertItemInSlot(inventory, stack, index, side)) {
            if (itemstack.isEmpty()) {
            	inventory.setInventorySlotContents(index, stack);
            	stack = ItemStack.EMPTY;
            }
            else if (canCombine(itemstack, stack)) {
                int i = stack.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemstack.grow(j);
            }
        }
        return stack;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getDamage() != stack2.getDamage() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }

// FLUID & INVENTORY CAPABILITIES STUFF

	protected IItemHandler createUnSidedHandler() {
		return new InventoryWrapperAH(this);
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluid().getAmount() / (float) tank.getCapacity() * scale) : 0;
	}

    @SuppressWarnings("unchecked")
	@Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return tank_holder.cast();
        
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return  itemholder.cast();
        return super.getCapability(capability, facing);
    }

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
		return new ContainerAbsorptionHopper(windowID, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Absorption Hopper"); //TODO localise
	}

}

