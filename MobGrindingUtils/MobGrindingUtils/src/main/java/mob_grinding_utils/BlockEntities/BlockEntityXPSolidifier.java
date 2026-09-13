package mob_grinding_utils.BlockEntities;

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
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockEntityXPSolidifier extends BlockEntity implements MenuProvider, BEGuiClickable {
	public FluidStacksResourceHandler tank = new FluidStacksResourceHandler(1, 1000 * 16);
	private int prevFluidLevel = 0;
	public int moulding_progress = 0;
	public int MAX_MOULDING_TIME = 100;
	public boolean isOn = false;
	private RecipeHolder<SolidifyRecipe> currentRecipe = null;

	public ItemStacksResourceHandler inputSlots = new ItemStacksResourceHandler(2);
	public ItemStacksResourceHandler outputSlot = new ItemStacksResourceHandler(1);

	public boolean active;
	public int animationTicks, prevAnimationTicks;

	public BlockEntityXPSolidifier(BlockPos pos, BlockState state) {
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

	public FluidStacksResourceHandler getTank(@Nullable Direction side) {
		return tank;
	}

	public ResourceHandler<ItemResource> getOutput(@Nullable Direction side) {
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
		if(t instanceof BlockEntityXPSolidifier tile) {
			if(tile.isOn) {
				if (level.isClientSide() && tile.active) {
					tile.prevAnimationTicks = tile.animationTicks;
					if (tile.animationTicks < tile.MAX_MOULDING_TIME)
						tile.animationTicks += 1 + tile.getModifierAmount();
					if (tile.animationTicks >= tile.MAX_MOULDING_TIME) {
						tile.animationTicks -= tile.MAX_MOULDING_TIME;
						tile.prevAnimationTicks -= tile.MAX_MOULDING_TIME;
					}
				}

				if (level.isClientSide() && !tile.active)
					tile.prevAnimationTicks = tile.animationTicks = 0;

				ItemStack mouldStack = ItemUtil.getStack(tile.inputSlots, 0);
				if (tile.currentRecipe != null) {
					if (!tile.currentRecipe.value().matches(mouldStack))
						tile.currentRecipe = null;
				} else {
					if (!mouldStack.isEmpty())
						tile.currentRecipe = getRecipeForMould(mouldStack);
				}


				if (tile.hasFluid() && tile.canOperate()) {
					tile.setActive(true);
					tile.setProgress(tile.getProgress() + 1 + tile.getModifierAmount());
					if (tile.getProgress() >= tile.MAX_MOULDING_TIME) {
						tile.setActive(false);
						ItemStack result = tile.currentRecipe.value().result();
						tile.outputSlot.set(0, ItemResource.of(result), result.getCount());
						try (Transaction tx = Transaction.openRoot()) {
							tile.tank.extract(0, tile.tank.getResource(0), tile.currentRecipe.value().fluidAmount(), tx);
							tx.commit();
						}
						return;
					}
				} else {
					if (tile.getProgress() > 0) {
						tile.setProgress(0);
						tile.setActive(false);
					}
				}

				if (!level.isClientSide() &&  tile.outputDirection != OutputDirection.NONE && tile.getOutputFacing() != null) {
					BlockEntity otherTile = level.getBlockEntity(worldPosition.relative(tile.getOutputFacing()));
					Optional<ResourceHandler<ItemResource>> handlerOptional = CapHelper.getItemHandler(level, worldPosition.relative(tile.getOutputFacing()), tile.getOutputFacing().getOpposite());
					if (otherTile != null && handlerOptional.isPresent()) {
						handlerOptional.ifPresent((handler) -> {
							ItemStack out = ItemUtil.getStack(tile.outputSlot, 0);
							if (!out.isEmpty()) {
								ItemStack stack = out.copy();
								stack.setCount(1);
								ItemStack stack1 = ItemUtil.insertItemReturnRemaining(handler, stack, true, null);
								if (stack1.isEmpty()) {
									ItemStack extracted = ItemUtil.getStack(tile.outputSlot, 0).copyWithCount(1);
									try (Transaction tx = Transaction.openRoot()) {
										tile.outputSlot.extract(0, ItemResource.of(extracted), 1, tx);
										tx.commit();
									}
									ItemUtil.insertItemReturnRemaining(handler, extracted, false, null);
									tile.setChanged();
								}
							}
						});
					} else if (otherTile instanceof Container iinventory) {
						if (tile.isInventoryFull(iinventory, tile.getOutputFacing()))
							return;
						ItemStack out = ItemUtil.getStack(tile.outputSlot, 0);
						if (!out.isEmpty()) {
							ItemStack stack = out.copy();
							ItemStack extracted = ItemUtil.getStack(tile.outputSlot, 0).copyWithCount(1);
							try (Transaction tx = Transaction.openRoot()) {
								tile.outputSlot.extract(0, ItemResource.of(extracted), 1, tx);
								tx.commit();
							}
							ItemStack stack1 = putStackInInventoryAllSlots(iinventory, extracted, tile.getOutputFacing().getOpposite());
							if (stack1.isEmpty() || stack1.getCount() == 0)
								iinventory.setChanged();
							else
								tile.outputSlot.set(0, ItemResource.of(stack), stack.getCount());
						}
					}
				}
			}
			else {
				if (level.isClientSide())
					tile.prevAnimationTicks = tile.animationTicks = 0;

				if (tile.getProgress() > 0) {
					tile.setActive(false);
					tile.setProgress(0);
				}
			}

			if (tile.prevFluidLevel != tile.tank.getAmountAsInt(0)){
				tile.updateBlock();
				tile.prevFluidLevel = tile.tank.getAmountAsInt(0);
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
	public ItemStack getCachedOutPutRenderStack() {
		if(hasMould()) {
			if(ItemUtil.getStack(inputSlots, 0).getItem() == ModItems.SOLID_XP_MOULD_BABY.get())
				return new ItemStack(ModItems.SOLID_XP_BABY.get(), 1);
		}
		return ItemStack.EMPTY;
	}
	public int getProgressScaled(int count) {
		return getProgress() * count / (MAX_MOULDING_TIME);
	}

	private boolean hasFluid() {
		FluidStack fluid = FluidUtil.getStack(tank, 0);
		return currentRecipe != null && !fluid.isEmpty() && fluid.getAmount() >= currentRecipe.value().fluidAmount() && fluid.getFluid().is(ModTags.Fluids.EXPERIENCE);
	}

	private boolean canOperate() {
		return hasMould() && isOutputEmpty();
	}

	private boolean hasMould() {
		return currentRecipe != null && currentRecipe.value().matches(ItemUtil.getStack(inputSlots, 0));
	}

	@Nullable
	public static RecipeHolder<SolidifyRecipe> getRecipeForMould(ItemStack stack) {
		return MobGrindingUtils.SOLIDIFIER_RECIPES.stream().filter(recipe -> recipe.value().matches(stack)).findFirst().orElse(null);
	}

	private boolean isOutputEmpty() {
		return ItemUtil.getStack(outputSlot, 0).isEmpty();
	}

	private boolean hasUpgrade() {
		ItemStack upgrade = ItemUtil.getStack(inputSlots, 1);
		return !upgrade.isEmpty() && upgrade.getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get();
	}

	public int getModifierAmount() {
		return hasUpgrade() ? ItemUtil.getStack(inputSlots, 1).getCount() : 0;
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
		if (inventory instanceof WorldlyContainer isidedinventory && facing != null && !(inventory instanceof BlockEntityXPSolidifier) && inventory.canPlaceItem(0, stack.copy())) {
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
	protected void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
		FluidStack fluidStack = input.read("tank", FluidStack.CODEC).orElse(FluidStack.EMPTY);
		tank.set(0, FluidResource.of(fluidStack), fluidStack.amount());
		inputSlots.deserialize(input.childOrEmpty("inputSlots"));
		outputSlot.deserialize(input.childOrEmpty("outputSlot"));
		outputDirection = OutputDirection.fromString(input.getStringOr("outputDirection", "north"));
		isOn = input.getBooleanOr("isOn", false);
		active = input.getBooleanOr("active", false);
		moulding_progress = input.getIntOr("moulding_progress", 0);
		input.getString("currentRecipe").ifPresent(recipeId -> {
			Identifier id = Identifier.tryParse(recipeId);
			MobGrindingUtils.SOLIDIFIER_RECIPES.stream().filter(recipe -> recipe.id().equals(id))
				.findFirst().ifPresent(recipe -> this.currentRecipe = recipe);
		});
	}

	@Override
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
		if (!tank.getResource(0).isEmpty() && tank.getAmountAsInt(0) > 0)
			output.store("tank", FluidStack.CODEC, FluidUtil.getStack(tank, 0));
		inputSlots.serialize(output.child("inputSlots"));
		outputSlot.serialize(output.child("outputSlot"));
		output.putString("outputDirection", outputDirection.getSerializedName());
		output.putBoolean("isOn", isOn);
		output.putBoolean("active", active);
		output.putInt("moulding_progress", moulding_progress);
		if (currentRecipe != null)
			output.putString("currentRecipe", currentRecipe.id().toString());
	}

	@Nonnull
	@Override
	public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
		return saveCustomOnly(registries);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ValueInput valueInput) {
		super.onDataPacket(net, valueInput);
		onContentsChanged();
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	public void onContentsChanged() {
		if (this.level != null && level.isClientSide()) {
			final BlockState state = level.getBlockState(getBlockPos());
			level.sendBlockUpdated(getBlockPos(), state, state, 8);
			setChanged();
		}
	}

	public int getScaledFluid(int scale) {
		return !tank.getResource(0).isEmpty() ? (int) ((float) tank.getAmountAsInt(0) / (float) tank.getCapacityAsInt(0, tank.getResource(0)) * scale) : 0;
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
	protected void applyImplicitComponents(@Nonnull DataComponentGetter componentInput) {
		super.applyImplicitComponents(componentInput);

		var fluidStack = componentInput.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get();
		tank.set(0, FluidResource.of(fluidStack), fluidStack.amount());
	}

	@Override
	protected void collectImplicitComponents(@Nonnull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(MGUComponents.FLUID, FluidContents.of(FluidUtil.getStack(tank, 0)));
	}
}
