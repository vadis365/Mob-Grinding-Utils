package mob_grinding_utils.tile;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.inventory.server.ContainerAbsorptionHopper;
import mob_grinding_utils.inventory.server.InventoryWrapperAH;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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


public class TileEntityAbsorptionHopper extends TileEntityInventoryHelper implements MenuProvider {
	public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME *  16);
	private final LazyOptional<IFluidHandler> tank_holder = LazyOptional.of(() -> tank);
	private final IItemHandler itemHandler;
	private LazyOptional<IItemHandler> itemholder = LazyOptional.empty();
	private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
	public int prevTankAmount;
	public TileEntityAbsorptionHopper(BlockPos pos, BlockState state) {
		super(ModBlocks.ABSORPTION_HOPPER.getTileEntityType(), 17, pos, state);
		itemHandler = createUnSidedHandler();
		itemholder = LazyOptional.of(() -> itemHandler);
	}

	public enum EnumStatus implements StringRepresentable {
		STATUS_NONE("none"),
		STATUS_OUTPUT_ITEM("item"),
		STATUS_OUTPUT_FLUID("fluid");

		private final String name;

		EnumStatus(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public EnumStatus[] status = new EnumStatus[] { EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE, EnumStatus.STATUS_NONE };
	public boolean showRenderBox;
	public int offsetX, offsetY, offsetZ;

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		EnumStatus[] old = new EnumStatus[] { status[0], status[1], status[2], status[3], status[4], status[5] };
		super.onDataPacket(net, packet);
		load(packet.getTag());
		for (Direction facing : Direction.values()) {
			if (old[facing.ordinal()] != status[facing.ordinal()]) {
				getLevel().setBlocksDirty(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()));
				return;
			}
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		return save(nbt);
	}

	@Override
	public void load(CompoundTag tagCompound) {
		super.load(tagCompound);
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
	public CompoundTag save(CompoundTag tagCompound) {
		super.save(tagCompound);
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
		setChanged();
	}

	public void toggleRenderBox() {
		showRenderBox = !showRenderBox;
		setChanged();
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
		setChanged();
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	public static <T extends BlockEntity> void serverTick(Level level, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof TileEntityAbsorptionHopper tile) {
			tile.prevTankAmount = tile.tank.getFluidAmount();
			for (Direction facing : Direction.values()) {
				if (tile.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_ITEM) {
					BlockEntity otherTile = level.getBlockEntity(worldPosition.relative(facing));
					if (otherTile != null && otherTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).isPresent()) {
						LazyOptional<IItemHandler> tileOptional = otherTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
						tileOptional.ifPresent((handler) -> {
							if (level.getGameTime() % 8 == 0) {
								for (int i = 0; i < tile.getContainerSize(); ++i) {
									if (!tile.getItem(i).isEmpty() && i != 0) {
										ItemStack stack = tile.getItem(i).copy();
										stack.setCount(1);
										ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
										if (stack1.isEmpty()) {
											ItemHandlerHelper.insertItem(handler, tile.removeItem(i, 1), false);
											tile.setChanged();
										}
									}
								}
							}
						});
					} else if (otherTile instanceof Container) {
						Container iinventory = (Container) otherTile;
						if (tile.isInventoryFull(iinventory, facing))
							break;
						else if (level.getGameTime() % 8 == 0) {
							for (int i = 0; i < tile.getContainerSize(); ++i) {
								if (!tile.getItem(i).isEmpty() && i != 0) {
									ItemStack stack = tile.getItem(i).copy();
									ItemStack stack1 = putStackInInventoryAllSlots(iinventory, tile.removeItem(i, 1), facing.getOpposite());
									if (stack1.isEmpty() || stack1.getCount() == 0)
										iinventory.setChanged();
									else
										tile.setItem(i, stack);
								}
							}
						}
					}
				}

				if (tile.status[facing.ordinal()] == EnumStatus.STATUS_OUTPUT_FLUID) {
					BlockEntity fluidTile = level.getBlockEntity(worldPosition.relative(facing));
					if (fluidTile != null) {
						LazyOptional<IFluidHandler> tileOptional = fluidTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
						tileOptional.ifPresent((receptacle) -> {
							int tanks = receptacle.getTanks();
							for (int x = 0; x < tanks; x++) {
								if (receptacle.getTankCapacity(x) > 0) {
									FluidStack contents = receptacle.getFluidInTank(x);
									if (!tile.tank.getFluid().isEmpty()) {
										if (contents.isEmpty() || contents.getAmount() <= receptacle.getTankCapacity(x) - 100 && contents.containsFluid(new FluidStack(tile.tank.getFluid(), 1))) {
											receptacle.fill(tile.tank.drain(new FluidStack(tile.tank.getFluid(), 100), FluidAction.EXECUTE), FluidAction.EXECUTE);
											tile.setChanged();
										}
									}
								}
							}
						});
					}
				}
			}


			if (level.getGameTime() % 3 == 0 && !level.hasNeighborSignal(worldPosition)) {
				if (!tile.isInventoryFull(tile, null))
					tile.captureDroppedItems();
				if (tile.tank.getFluid().isEmpty() || tile.tank.getFluid().containsFluid(new FluidStack(ModBlocks.FLUID_XP.get(), 1)))
					tile.captureDroppedXP();
			}

			if (tile.prevTankAmount != tile.tank.getFluidAmount())
				tile.updateBlock();
		}
	}

	@Override
	@Nullable
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(getItems(), index, count);
	}

	public boolean captureDroppedItems() {
		for (ItemEntity entityitem : getCaptureItems())
			if (putDropInInventoryAllSlots(this, entityitem))
				return true;
		return false;
	}

	public List<ItemEntity> getCaptureItems() {
		return getLevel().<ItemEntity>getEntitiesOfClass(ItemEntity.class, getAABBWithModifiers(), EntitySelector.ENTITY_STILL_ALIVE);
	}

	public boolean captureDroppedXP() {
		for (ExperienceOrb entity : getCaptureXP()) {
			int xpAmount = entity.getValue();
			if (tank.getFluidAmount() < tank.getCapacity() - xpAmount * 20) {
				tank.fill(new FluidStack(ModBlocks.FLUID_XP.get(), xpAmount * 20), FluidAction.EXECUTE);
				entity.value = 0;
				entity.remove(Entity.RemovalReason.DISCARDED);
			}
			return true;
		}
		return false;
	}

	public List<ExperienceOrb> getCaptureXP() {
		return getLevel().<ExperienceOrb>getEntitiesOfClass(ExperienceOrb.class, getAABBWithModifiers(), EntitySelector.ENTITY_STILL_ALIVE);
	}

	public AABB getAABBWithModifiers() {
		double x = getBlockPos().getX() + 0.5D;
		double y = getBlockPos().getY() + 0.5D;
		double z = getBlockPos().getZ() + 0.5D;
		return new AABB(x - 3.5D - getModifierAmount(), y - 3.5D - getModifierAmount(), z - 3.5D - getModifierAmount(), x + 3.5D + getModifierAmount(), y + 3.5D + getModifierAmount(), z + 3.5D + getModifierAmount()).move(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@OnlyIn(Dist.CLIENT)
	public AABB getAABBForRender() {
		return new AABB(- 3D - getModifierAmount(), - 3D - getModifierAmount(), - 3D - getModifierAmount(), 4D + getModifierAmount(), 4D + getModifierAmount(), 4D + getModifierAmount()).move(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AABB getRenderBoundingBox() {
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
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(getItems(), index);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return slot != 0;
	}

	@Nonnull
	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		return slot != 0;
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
		if (inventory instanceof WorldlyContainer && facing != null && !(inventory instanceof TileEntityAbsorptionHopper) && inventory.canPlaceItem(0, stack.copy())) {
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

	public static boolean putDropInInventoryAllSlots(Container inventoryIn, ItemEntity itemIn) {
		boolean flag = false;

		if (itemIn == null || inventoryIn instanceof TileEntityAbsorptionHopper && inventoryIn.canPlaceItem(0, itemIn.getItem().copy())) {
			return false;
		} else {
			ItemStack itemstack = itemIn.getItem().copy();
			ItemStack itemstack1 = putStackInInventoryAllSlots(inventoryIn, itemstack, (Direction) null);

			if (!itemstack1.isEmpty()) {
				itemIn.setItem(itemstack1);
			} else {
				flag = true;
				itemIn.remove(Entity.RemovalReason.DISCARDED);
			}
			return flag;
		}
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
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player) {
		return new ContainerAbsorptionHopper(windowID, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(worldPosition));
	}

	@Nonnull
	@Override
	public Component getDisplayName() {
		return new TextComponent("Absorption Hopper"); //TODO localise
	}

}

