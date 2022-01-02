package mob_grinding_utils.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.text.html.Option;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityXPSolidifier extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
	public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME *  16);
	private final LazyOptional<IFluidHandler> tank_holder = LazyOptional.of(() -> tank);
	private int prevFluidLevel = 0;
	public int moulding_progress = 0;
	public int MAX_MOULDING_TIME = 100;
	public boolean isOn = false;
	private SolidifyRecipe currentRecipe = null;

	public ItemStackHandler inputSlots = new ItemStackHandler(2);
	public ItemStackHandler outputSlot = new ItemStackHandler(1);
	private final LazyOptional<IItemHandler> outputCap = LazyOptional.of(() -> outputSlot);

	public boolean active;
	public int animationTicks, prevAnimationTicks;

	public TileEntityXPSolidifier() {
		super(ModBlocks.XPSOLIDIFIER.getTileEntityType());
	}

	public enum OutputDirection implements IStringSerializable {
		NONE("none"),
		NORTH("north"),
		EAST("east"),
		SOUTH("south"),
		WEST("west");

		String name;
		OutputDirection(String nameIn) {name = nameIn;}

		@Override
		public String getString() { return name; }

		public static OutputDirection fromString(String string) {
			for (OutputDirection direction : OutputDirection.values())
				if (direction.name.equals(string))
					return direction;
			return OutputDirection.NONE;
		}
	}

	public OutputDirection outputDirection = OutputDirection.NONE;

	public OutputDirection toggleOutput() {
		switch(outputDirection) {
			case WEST:
				outputDirection = OutputDirection.NONE;
				break;
			case SOUTH:
				outputDirection = OutputDirection.WEST;
				break;
			case EAST:
				outputDirection = OutputDirection.SOUTH;
				break;
			case NORTH:
				outputDirection = OutputDirection.EAST;
				break;
			case NONE:
				outputDirection = OutputDirection.NORTH;
				break;
		}
		markDirty();
		return outputDirection;
	}

	public void toggleOnOff() {
		isOn = !isOn;
	}

	@Override
	public void tick() {
		if(isOn) {
			if (getWorld().isRemote && active) {
				prevAnimationTicks = animationTicks;
				if (animationTicks < MAX_MOULDING_TIME)
					animationTicks += 1 + getModifierAmount();
				if (animationTicks >= MAX_MOULDING_TIME) {
					animationTicks -= MAX_MOULDING_TIME;
					prevAnimationTicks -= MAX_MOULDING_TIME;
				}
			}

			if (getWorld().isRemote && !active)
				prevAnimationTicks = animationTicks = 0;

			if (currentRecipe != null) {
				if (!currentRecipe.matches(inputSlots.getStackInSlot(0)))
					currentRecipe = null;
			} else
				currentRecipe = getRecipeForMould(inputSlots.getStackInSlot(0));


			if (hasfluid() && canOperate()) {
				setActive(true);
				setProgress(getProgress() + 1 + getModifierAmount());
				if (getProgress() >= MAX_MOULDING_TIME) {
					setActive(false);
					outputSlot.setStackInSlot(0, currentRecipe.getResult());
					tank.drain(currentRecipe.getFluidAmount(), FluidAction.EXECUTE);
					return;
				}
			} else {
				if (getProgress() > 0) {
					setProgress(0);
					setActive(false);
				}
			}

			if (outputDirection != OutputDirection.NONE && getOutputFacing() != null) {
				TileEntity tile = getWorld().getTileEntity(pos.offset(getOutputFacing()));
				if (tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutputFacing().getOpposite()).isPresent()) {
					LazyOptional<IItemHandler> tileOptional = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutputFacing().getOpposite());
					tileOptional.ifPresent((handler) -> {
						if (!outputSlot.getStackInSlot(0).isEmpty()) {
							ItemStack stack = outputSlot.getStackInSlot(0).copy();
							stack.setCount(1);
							ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
							if (stack1.isEmpty()) {
								ItemHandlerHelper.insertItem(handler, outputSlot.extractItem(0, 1, false), false);
								this.markDirty();
							}
						}
					});
				} else if (tile != null && tile instanceof IInventory) {
					IInventory iinventory = (IInventory) tile;
					if (isInventoryFull(iinventory, getOutputFacing()))
						return;
					if (!outputSlot.getStackInSlot(0).isEmpty()) {
						ItemStack stack = outputSlot.getStackInSlot(0).copy();
						ItemStack stack1 = putStackInInventoryAllSlots(iinventory, outputSlot.extractItem(0, 1, false), getOutputFacing().getOpposite());
						if (stack1.isEmpty() || stack1.getCount() == 0)
							iinventory.markDirty();
						else
							outputSlot.setStackInSlot(0, stack);
					}
				}
			}
		}
		else {
			if (getWorld().isRemote)
				prevAnimationTicks = animationTicks = 0;

			if (getProgress() > 0) {
				setActive(false);
				setProgress(0);
			}
		}

		if (prevFluidLevel != tank.getFluidAmount()){
			updateBlock();
			prevFluidLevel = tank.getFluidAmount();
		}
	}

	public void setActive(boolean isActive) {
		active = isActive;
	}

	private Direction getOutputFacing() {
		switch (outputDirection) {
			case WEST:
				return Direction.WEST;
			case SOUTH:
				return Direction.SOUTH;
			case EAST:
				return Direction.EAST;
			case NORTH:
				return Direction.NORTH;
			case NONE:
				break;
			default:
				break;
		}
		return null;

	}

	@OnlyIn(Dist.CLIENT)
	public ItemStack getCachedOutPutRenderStack() { // TODO this needs to be cached to NBT probably
		if(hasMould()) {
			if(inputSlots.getStackInSlot(0).getItem() == ModItems.SOLID_XP_MOULD_BABY.get())
				return new ItemStack(ModItems.SOLID_XP_BABY.get(), 1);
		}
		return ItemStack.EMPTY;
	}

	@OnlyIn(Dist.CLIENT)
	public int getProgressScaled(int count) {
		return getProgress() * count / (MAX_MOULDING_TIME);
	}

	private boolean hasfluid() {
		return currentRecipe != null && !tank.getFluid().isEmpty() && tank.getFluid().getAmount() >= currentRecipe.getFluidAmount() && tank.getFluidInTank(0).getFluid().isIn(MobGrindingUtils.EXPERIENCE);
	}

	private boolean canOperate() {
		return hasMould() && isOutputEmpty();
	}

	private boolean hasMould() {
		return currentRecipe != null && currentRecipe.matches(inputSlots.getStackInSlot(0));
	}

	@Nullable
	public static SolidifyRecipe getRecipeForMould(ItemStack stack) {
		return MobGrindingUtils.SOLIDIFIER_RECIPES.stream().filter(recipe -> recipe.matches(stack)).findFirst().orElse(null);
	}

	private boolean isOutputEmpty() {
		return outputSlot.getStackInSlot(0).isEmpty();
	}

	private boolean hasUpgrade() {
		return !inputSlots.getStackInSlot(1).isEmpty() && inputSlots.getStackInSlot(1).getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get();
	}

	public int getModifierAmount() {
		return hasUpgrade() ? inputSlots.getStackInSlot(1).getCount() : 0;
	}

	private void setProgress(int counter) {
		moulding_progress = counter;
	}

	public int getProgress() {
		return moulding_progress;
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
		if (inventory instanceof ISidedInventory && facing != null && !(inventory instanceof TileEntityXPSolidifier) && inventory.isItemValidForSlot(0, stack.copy())) {
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

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		tank.readFromNBT(nbt);
		inputSlots.deserializeNBT(nbt.getCompound("inputSlots"));
		outputSlot.deserializeNBT(nbt.getCompound("outputSlot"));
		outputDirection = OutputDirection.fromString(nbt.getString("outputDirection"));
		isOn = nbt.getBoolean("isOn");
		active = nbt.getBoolean("active");
		moulding_progress = nbt.getInt("moulding_progress");
		if (nbt.contains("currentRecipe")) {
			ResourceLocation id = new ResourceLocation(nbt.getString("currentRecipe"));
			MobGrindingUtils.SOLIDIFIER_RECIPES.stream().filter(recipe -> recipe.getId().equals(id))
				.findFirst().ifPresent(recipe -> this.currentRecipe = recipe);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		tank.writeToNBT(nbt);
		nbt.put("inputSlots", inputSlots.serializeNBT());
		nbt.put("outputSlot", outputSlot.serializeNBT());
		nbt.putString("outputDirection", outputDirection.getString());
		nbt.putBoolean("isOn", isOn);
		nbt.putBoolean("active", active);
		nbt.putInt("moulding_progress", moulding_progress);
		if (currentRecipe != null)
			nbt.putString("currentRecipe", currentRecipe.getId().toString());
		return nbt;
	}
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		return write(nbt);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(getPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		read(getBlockState(), packet.getNbtCompound());
		onContentsChanged();
	}

	public void updateBlock() {
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}

	public void onContentsChanged() {
		if (this != null && !getWorld().isRemote) {
			final BlockState state = getWorld().getBlockState(getPos());
			getWorld().notifyBlockUpdate(getPos(), state, state, 8);
			markDirty();
		}
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluid().getAmount() / (float) tank.getCapacity() * scale) : 0;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("block.mob_grinding_utils.xpsolidifier");
	}

	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
		return new ContainerXPSolidifier(windowID, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return tank_holder.cast();
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return outputCap.cast();
		return super.getCapability(cap, side);
	}


}
