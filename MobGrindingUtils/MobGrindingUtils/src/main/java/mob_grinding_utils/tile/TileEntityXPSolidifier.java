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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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

public class TileEntityXPSolidifier extends BlockEntity implements TickableBlockEntity, MenuProvider {
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

	public enum OutputDirection implements StringRepresentable {
		NONE("none"),
		NORTH("north"),
		EAST("east"),
		SOUTH("south"),
		WEST("west");

		String name;
		OutputDirection(String nameIn) {name = nameIn;}

		@Override
		public String getSerializedName() { return name; }

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
		setChanged();
		return outputDirection;
	}

	public void toggleOnOff() {
		isOn = !isOn;
	}

	@Override
	public void tick() {
		if(isOn) {
			if (getLevel().isClientSide && active) {
				prevAnimationTicks = animationTicks;
				if (animationTicks < MAX_MOULDING_TIME)
					animationTicks += 1 + getModifierAmount();
				if (animationTicks >= MAX_MOULDING_TIME) {
					animationTicks -= MAX_MOULDING_TIME;
					prevAnimationTicks -= MAX_MOULDING_TIME;
				}
			}

			if (getLevel().isClientSide && !active)
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
				BlockEntity tile = getLevel().getBlockEntity(worldPosition.relative(getOutputFacing()));
				if (tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutputFacing().getOpposite()).isPresent()) {
					LazyOptional<IItemHandler> tileOptional = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getOutputFacing().getOpposite());
					tileOptional.ifPresent((handler) -> {
						if (!outputSlot.getStackInSlot(0).isEmpty()) {
							ItemStack stack = outputSlot.getStackInSlot(0).copy();
							stack.setCount(1);
							ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
							if (stack1.isEmpty()) {
								ItemHandlerHelper.insertItem(handler, outputSlot.extractItem(0, 1, false), false);
								this.setChanged();
							}
						}
					});
				} else if (tile != null && tile instanceof Container) {
					Container iinventory = (Container) tile;
					if (isInventoryFull(iinventory, getOutputFacing()))
						return;
					if (!outputSlot.getStackInSlot(0).isEmpty()) {
						ItemStack stack = outputSlot.getStackInSlot(0).copy();
						ItemStack stack1 = putStackInInventoryAllSlots(iinventory, outputSlot.extractItem(0, 1, false), getOutputFacing().getOpposite());
						if (stack1.isEmpty() || stack1.getCount() == 0)
							iinventory.setChanged();
						else
							outputSlot.setStackInSlot(0, stack);
					}
				}
			}
		}
		else {
			if (getLevel().isClientSide)
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
		return currentRecipe != null && !tank.getFluid().isEmpty() && tank.getFluid().getAmount() >= currentRecipe.getFluidAmount() && tank.getFluidInTank(0).getFluid().is(MobGrindingUtils.EXPERIENCE);
	}

	private boolean canOperate() {
		return hasMould() && isOutputEmpty();
	}

	private boolean hasMould() {
		return currentRecipe != null && currentRecipe.matches(inputSlots.getStackInSlot(0));
	}

	@Nullable
	public static SolidifyRecipe getRecipeForMould(ItemStack stack) {
		for (SolidifyRecipe recipe : MobGrindingUtils.SOLIDIFIER_RECIPES) {
			if(recipe.matches(stack))
				return recipe;
		}
		return null;
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

	private boolean isInventoryFull(Container inventoryIn, Direction side) {
		if (inventoryIn instanceof WorldlyContainer) {
			WorldlyContainer isidedinventory = (WorldlyContainer) inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for (int k : aint) {
				ItemStack itemstack1 = isidedinventory.getItem(k);

				if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
					return false;
			}
		} else {
			int i = inventoryIn.getContainerSize();

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = inventoryIn.getItem(j);

				if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
					return false;
			}
		}

		return true;
	}

	public static ItemStack putStackInInventoryAllSlots(Container inventory, ItemStack stack, @Nullable Direction facing) {
		if (inventory instanceof WorldlyContainer && facing != null && !(inventory instanceof TileEntityXPSolidifier) && inventory.canPlaceItem(0, stack.copy())) {
			WorldlyContainer isidedinventory = (WorldlyContainer)inventory;
			int[] aint = isidedinventory.getSlotsForFace(facing);
			for (int k = 0; k < aint.length && !stack.isEmpty(); ++k)
				stack = insertStack(inventory, stack, aint[k], facing);
		} else {
			int i = inventory.getContainerSize();
			for (int j = 0; j < i && !stack.isEmpty(); ++j)
				stack = insertStack(inventory, stack, j, facing);
		}
		return stack;
	}

	private static boolean canInsertItemInSlot(Container inventoryIn, ItemStack stack, int index, Direction side) {
		return inventoryIn.canPlaceItem(index, stack) && (!(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer) inventoryIn).canPlaceItemThroughFace(index, stack, side));
	}

	private static ItemStack insertStack( Container inventory, ItemStack stack, int index, Direction side) {
		ItemStack itemstack = inventory.getItem(index);
		if (canInsertItemInSlot(inventory, stack, index, side)) {
			if (itemstack.isEmpty()) {
				inventory.setItem(index, stack);
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
		return stack1.getItem() != stack2.getItem() ? false : (stack1.getDamageValue() != stack2.getDamageValue() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.tagMatches(stack1, stack2)));
	}

	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		tank.readFromNBT(nbt);
		inputSlots.deserializeNBT(nbt.getCompound("inputSlots"));
		outputSlot.deserializeNBT(nbt.getCompound("outputSlot"));
		outputDirection = OutputDirection.fromString(nbt.getString("outputDirection"));
		isOn = nbt.getBoolean("isOn");
		active = nbt.getBoolean("active");
		moulding_progress = nbt.getInt("moulding_progress");
		if (nbt.contains("currentRecipe")) {
			ResourceLocation id = new ResourceLocation(nbt.getString("currentRecipe"));
			for (SolidifyRecipe recipe : MobGrindingUtils.SOLIDIFIER_RECIPES) {
				if (recipe.getId().equals(id)) {
					this.currentRecipe = recipe;
					break;
				}
			}
		}
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		tank.writeToNBT(nbt);
		nbt.put("inputSlots", inputSlots.serializeNBT());
		nbt.put("outputSlot", outputSlot.serializeNBT());
		nbt.putString("outputDirection", outputDirection.getSerializedName());
		nbt.putBoolean("isOn", isOn);
		nbt.putBoolean("active", active);
		nbt.putInt("moulding_progress", moulding_progress);
		if (currentRecipe != null)
			nbt.putString("currentRecipe", currentRecipe.getId().toString());
		return nbt;
	}
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		return save(nbt);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		load(getBlockState(), packet.getTag());
		onContentsChanged();
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	public void onContentsChanged() {
		if (this != null && !getLevel().isClientSide) {
			final BlockState state = getLevel().getBlockState(getBlockPos());
			getLevel().sendBlockUpdated(getBlockPos(), state, state, 8);
			setChanged();
		}
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluid().getAmount() / (float) tank.getCapacity() * scale) : 0;
	}

	@Override
	public Component getDisplayName() {
		return new TextComponent("block.mob_grinding_utils.xpsolidifier");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player) {
		return new ContainerXPSolidifier(windowID, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(worldPosition));
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
