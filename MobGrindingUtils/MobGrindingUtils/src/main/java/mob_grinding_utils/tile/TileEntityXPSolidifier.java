package mob_grinding_utils.tile;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TileEntityXPSolidifier extends BlockEntity implements MenuProvider, BEGuiClickable {
	public FluidTank tank = new FluidTank(1000 *  16);
	private int prevFluidLevel = 0;
	public int moulding_progress = 0;
	public int MAX_MOULDING_TIME = 100;
	public boolean isOn = false;
	private RecipeHolder<SolidifyRecipe> currentRecipe = null;

	public ItemStackHandler inputSlots = new ItemStackHandler(2);
	public ItemStackHandler outputSlot = new ItemStackHandler(1);

	public boolean active;
	public int animationTicks, prevAnimationTicks;

	public TileEntityXPSolidifier(BlockPos pos, BlockState state) {
		super(ModBlocks.XPSOLIDIFIER.getTileEntityType(), pos, state);
	}

	@Override
	public void buttonClicked(int buttonID) {
		switch (buttonID) {
			case 0 -> toggleOutput();
			case 1 -> toggleOnOff();
		}
		updateBlock();
	}

	public enum OutputDirection implements StringRepresentable {
		NONE("none"),
		NORTH("north"),
		EAST("east"),
		SOUTH("south"),
		WEST("west");

		final String name;
		OutputDirection(String nameIn) { name = nameIn; }

		@Nonnull
		@Override
		public String getSerializedName() { return name; }

		public static OutputDirection fromString(String string) {
			for (OutputDirection direction : OutputDirection.values())
				if (direction.name.equals(string))
					return direction;
			return OutputDirection.NONE;
		}
	}

	public FluidTank getTank(@Nullable Direction side) {
		return tank;
	}
	public IItemHandler getOutput(@Nullable Direction side) {
		return outputSlot;
	}

	public OutputDirection outputDirection = OutputDirection.NONE;

	public void toggleOutput() {
		switch (outputDirection) {
			case WEST -> outputDirection = OutputDirection.NONE;
			case SOUTH -> outputDirection = OutputDirection.WEST;
			case EAST -> outputDirection = OutputDirection.SOUTH;
			case NORTH -> outputDirection = OutputDirection.EAST;
			case NONE -> outputDirection = OutputDirection.NORTH;
		}
		setChanged();
	}

	public void toggleOnOff() {
		isOn = !isOn;
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos worldPosition, BlockState blockState, T t) {
		if(t instanceof TileEntityXPSolidifier tile) {
			if(tile.isOn) {
				if (level.isClientSide && tile.active) {
					tile.prevAnimationTicks = tile.animationTicks;
					if (tile.animationTicks < tile.MAX_MOULDING_TIME)
						tile.animationTicks += 1 + tile.getModifierAmount();
					if (tile.animationTicks >= tile.MAX_MOULDING_TIME) {
						tile.animationTicks -= tile.MAX_MOULDING_TIME;
						tile.prevAnimationTicks -= tile.MAX_MOULDING_TIME;
					}
				}

				if (level.isClientSide && !tile.active)
					tile.prevAnimationTicks = tile.animationTicks = 0;

				if (tile.currentRecipe != null) {
					if (!tile.currentRecipe.value().matches(tile.inputSlots.getStackInSlot(0)))
						tile.currentRecipe = null;
				} else {
					if (!tile.inputSlots.getStackInSlot(0).isEmpty())
						tile.currentRecipe = getRecipeForMould(tile.inputSlots.getStackInSlot(0));
				}


				if (tile.hasFluid() && tile.canOperate()) {
					tile.setActive(true);
					tile.setProgress(tile.getProgress() + 1 + tile.getModifierAmount());
					if (tile.getProgress() >= tile.MAX_MOULDING_TIME) {
						tile.setActive(false);
						tile.outputSlot.setStackInSlot(0, tile.currentRecipe.value().result());
						tile.tank.drain(tile.currentRecipe.value().fluidAmount(), IFluidHandler.FluidAction.EXECUTE);
						return;
					}
				} else {
					if (tile.getProgress() > 0) {
						tile.setProgress(0);
						tile.setActive(false);
					}
				}

				if (!level.isClientSide &&  tile.outputDirection != OutputDirection.NONE && tile.getOutputFacing() != null) {
					BlockEntity otherTile = level.getBlockEntity(worldPosition.relative(tile.getOutputFacing()));
					Optional<IItemHandler> handlerOptional = CapHelper.getItemHandler(level, worldPosition.relative(tile.getOutputFacing()), tile.getOutputFacing().getOpposite());
					if (otherTile != null && handlerOptional.isPresent()) {
						handlerOptional.ifPresent((handler) -> {
							if (!tile.outputSlot.getStackInSlot(0).isEmpty()) {
								ItemStack stack = tile.outputSlot.getStackInSlot(0).copy();
								stack.setCount(1);
								ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
								if (stack1.isEmpty()) {
									ItemHandlerHelper.insertItem(handler, tile.outputSlot.extractItem(0, 1, false), false);
									tile.setChanged();
								}
							}
						});
					} else if (otherTile instanceof Container iinventory) {
						if (tile.isInventoryFull(iinventory, tile.getOutputFacing()))
							return;
						if (!tile.outputSlot.getStackInSlot(0).isEmpty()) {
							ItemStack stack = tile.outputSlot.getStackInSlot(0).copy();
							ItemStack stack1 = putStackInInventoryAllSlots(iinventory, tile.outputSlot.extractItem(0, 1, false), tile.getOutputFacing().getOpposite());
							if (stack1.isEmpty() || stack1.getCount() == 0)
								iinventory.setChanged();
							else
								tile.outputSlot.setStackInSlot(0, stack);
						}
					}
				}
			}
			else {
				if (level.isClientSide)
					tile.prevAnimationTicks = tile.animationTicks = 0;

				if (tile.getProgress() > 0) {
					tile.setActive(false);
					tile.setProgress(0);
				}
			}

			if (tile.prevFluidLevel != tile.tank.getFluidAmount()){
				tile.updateBlock();
				tile.prevFluidLevel = tile.tank.getFluidAmount();
			}
		}
	}

	public void setActive(boolean isActive) {
		active = isActive;
	}

	private Direction getOutputFacing() {
        return switch (outputDirection) {
            case WEST -> Direction.WEST;
            case SOUTH -> Direction.SOUTH;
            case EAST -> Direction.EAST;
            case NORTH -> Direction.NORTH;
            default -> null;
        };

    }

	@OnlyIn(Dist.CLIENT)
	public ItemStack getCachedOutPutRenderStack() {
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

	private boolean hasFluid() {
		return currentRecipe != null && !tank.getFluid().isEmpty() && tank.getFluid().getAmount() >= currentRecipe.value().fluidAmount() && tank.getFluidInTank(0).getFluid().is(ModTags.Fluids.EXPERIENCE);
	}

	private boolean canOperate() {
		return hasMould() && isOutputEmpty();
	}

	private boolean hasMould() {
		return currentRecipe != null && currentRecipe.value().matches(inputSlots.getStackInSlot(0));
	}

	@Nullable
	public static RecipeHolder<SolidifyRecipe> getRecipeForMould(ItemStack stack) {
		return MobGrindingUtils.SOLIDIFIER_RECIPES.stream().filter(recipe -> recipe.value().matches(stack)).findFirst().orElse(null);
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
		if (inventoryIn instanceof WorldlyContainer isidedinventory) {
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
		if (inventory instanceof WorldlyContainer isidedinventory && facing != null && !(inventory instanceof TileEntityXPSolidifier) && inventory.canPlaceItem(0, stack.copy())) {
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
		return stack1.getItem() != stack2.getItem() ? false : (stack1.getDamageValue() != stack2.getDamageValue() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.isSameItemSameComponents(stack1, stack2)));
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		tank.readFromNBT(registries, nbt);
		inputSlots.deserializeNBT(registries, nbt.getCompound("inputSlots"));
		outputSlot.deserializeNBT(registries, nbt.getCompound("outputSlot"));
		outputDirection = OutputDirection.fromString(nbt.getString("outputDirection"));
		isOn = nbt.getBoolean("isOn");
		active = nbt.getBoolean("active");
		moulding_progress = nbt.getInt("moulding_progress");
		if (nbt.contains("currentRecipe")) {
			ResourceLocation id = ResourceLocation.tryParse(nbt.getString("currentRecipe"));
			MobGrindingUtils.SOLIDIFIER_RECIPES.stream().filter(recipe -> recipe.id().equals(id))
				.findFirst().ifPresent(recipe -> this.currentRecipe = recipe);
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		tank.writeToNBT(registries, nbt);
		nbt.put("inputSlots", inputSlots.serializeNBT(registries));
		nbt.put("outputSlot", outputSlot.serializeNBT(registries));
		nbt.putString("outputDirection", outputDirection.getSerializedName());
		nbt.putBoolean("isOn", isOn);
		nbt.putBoolean("active", active);
		nbt.putInt("moulding_progress", moulding_progress);
		if (currentRecipe != null)
			nbt.putString("currentRecipe", currentRecipe.id().toString());
	}

	@Nonnull
	@Override
	public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt, registries);
		return nbt;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt, level.registryAccess());
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, @Nonnull HolderLookup.Provider registries) {
		super.onDataPacket(net, packet, registries);
		loadAdditional(packet.getTag(), registries);
		onContentsChanged();
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	public void onContentsChanged() {
		if (this.level != null && level.isClientSide) {
			final BlockState state = level.getBlockState(getBlockPos());
			level.sendBlockUpdated(getBlockPos(), state, state, 8);
			setChanged();
		}
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluid().getAmount() / (float) tank.getCapacity() * scale) : 0;
	}

	@Nonnull
	@Override
	public Component getDisplayName() {
		return Component.translatable("block.mob_grinding_utils.xpsolidifier");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory, @Nonnull Player player) {
		return new ContainerXPSolidifier(windowID, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(worldPosition));
	}

	@Override
	protected void applyImplicitComponents(@Nonnull DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);

		tank.setFluid(componentInput.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get());
	}

	@Override
	protected void collectImplicitComponents(@Nonnull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(MGUComponents.FLUID, FluidContents.of(tank.getFluid()));
	}
}
