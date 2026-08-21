package mob_grinding_utils.tile;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileEntityMGUSpawner extends BlockEntity implements MenuProvider, BEGuiClickable {

	public int spawning_progress = 0;
	public int MAX_SPAWNING_TIME = 100;
	public boolean isOn = false;

	public final ItemStacksResourceHandler inputSlots = new ItemStacksResourceHandler(4) {
		@Override protected void onContentsChanged(int slot, ItemStack previousContents) { setChanged(); }
	};
	public final ItemStacksResourceHandler fuelSlot = new ItemStacksResourceHandler(1) {
		@Override
		public boolean isValid(int slot, ItemResource resource) {
			return resource.getItem() == ModItems.SOLID_XP_BABY.get();
		}
		@Override protected void onContentsChanged(int slot, ItemStack previousContents) { setChanged(); }
	};

	public int animationTicks, prevAnimationTicks;
	public boolean showRenderBox;
	public int offsetX, offsetY, offsetZ;

	public TileEntityMGUSpawner(BlockPos pos, BlockState state) {
		super(ModBlocks.ENTITY_SPAWNER.getTileEntityType(), pos, state);
	}

	public ResourceHandler<ItemResource> getFuelSlot(@Nullable Direction side) {
		return fuelSlot;
	}

	private static ItemStack stack(ItemStacksResourceHandler inventory, int slot) {
		return inventory.getResource(slot).toStack(inventory.getAmountAsInt(slot));
	}

	private static void setStack(ItemStacksResourceHandler inventory, int slot, ItemStack stack) {
		inventory.set(slot, ItemResource.of(stack), stack.getCount());
	}

	public ItemStack getInputStack(int slot) { return stack(inputSlots, slot); }
	public ItemStack getFuelStack() { return stack(fuelSlot, 0); }

	public void toggleOnOff() {
		isOn = !isOn;
	}

	public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof TileEntityMGUSpawner tile) {
			if (tile.isOn) {
				if (tile.canOperate()) {
					tile.setProgress(tile.getProgress() + 1 + tile.getSpeedModifierAmount());
					if (tile.getProgress() >= tile.MAX_SPAWNING_TIME) {
						if (tile.spawnMobInArea())
							setStack(tile.fuelSlot, 0, stack(tile.fuelSlot, 0).copyWithCount(stack(tile.fuelSlot, 0).getCount() - 1));
						tile.setProgress(0);
					}
				} else {
					if (tile.getProgress() > 0)
						tile.setProgress(0);
				}
			}
			else {
				if (tile.getProgress() > 0)
					tile.setProgress(0);
			}
		}
	}
	public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof TileEntityMGUSpawner tile) {
			if (tile.isOn) {
				tile.prevAnimationTicks = tile.animationTicks;
				if (tile.animationTicks < 360)
					tile.animationTicks += 9;
				if (tile.animationTicks >= 360) {
					tile.animationTicks -= 360;
					tile.prevAnimationTicks -= 360;
				}
			}
			else {
				tile.prevAnimationTicks = tile.animationTicks = 0;
			}
		}
	}

	private boolean spawnMobInArea() {
		EntityType<?> type = null;
		ItemStack eggStack = stack(inputSlots, 0);
		SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
		type = eggItem.getType(eggStack);

		if (type != null && !type.builtInRegistryHolder().is(ModTags.Entities.NO_SPAWN)) {
			AABB axisalignedbb = getAABBWithModifiers();
			int minX = Mth.floor(axisalignedbb.minX);
			int maxX = Mth.floor(axisalignedbb.maxX);
			int minY = Mth.floor(axisalignedbb.minY);
			int maxY = Mth.floor(axisalignedbb.maxY);
			int minZ = Mth.floor(axisalignedbb.minZ);
			int maxZ = Mth.floor(axisalignedbb.maxZ);
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
			Mob entity = (Mob) type.create(getLevel(), EntitySpawnReason.SPAWNER);
			List<BlockPos> posArrayList = new ArrayList<BlockPos>();
			if (entity != null) {
				for (int x = minX; x < maxX; x++) {
					for (int y = minY; y < maxY; y++) {
						for (int z = minZ; z < maxZ; z++) {
							mutablePos.set(x, y, z);
							entity.setPos(mutablePos.getX() + 0.5D, mutablePos.getY(), mutablePos.getZ() + 0.5D);
							if (isValidSpawnLocation(getLevel(), entity)) {
								posArrayList.add(new BlockPos(mutablePos));
							}
						}
					}
				}
				if (!posArrayList.isEmpty()) {
					Collections.shuffle(posArrayList);
					entity.setPos(posArrayList.get(0).getX() + 0.5D, posArrayList.get(0).getY(), posArrayList.get(0).getZ() + 0.5D);
					EventHooks.finalizeMobSpawn(entity, (ServerLevelAccessor) getLevel(), ((ServerLevelAccessor) getLevel()).getCurrentDifficultyAt(posArrayList.getFirst()), EntitySpawnReason.SPAWNER, null);
					getLevel().addFreshEntity(entity);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidSpawnLocation(Level world, Mob entity) {
		return EventHooks.checkSpawnPosition(entity, (ServerLevelAccessor) world, EntitySpawnReason.SPAWNER) && world.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && getLevel().noCollision(entity);
	}

	public void toggleRenderBox() {
		showRenderBox = !showRenderBox;
		setChanged();
	}

	public void toggleOffset(int direction) {
		switch (direction) {
			case 1:
				if (getoffsetY() >= -1 - getHeightModifierAmount())
					offsetY = getoffsetY() - 1;
				break;
			case 2:
				if (getoffsetY() <= 1 + getHeightModifierAmount())
					offsetY = getoffsetY() + 1;
				break;
			case 3:
				if (getoffsetZ() >= -1 - getWidthModifierAmount())
					offsetZ = getoffsetZ() - 1;
				break;
			case 4:
				if (getoffsetZ() <= 1 + getWidthModifierAmount())
					offsetZ = getoffsetZ() + 1;
				break;
			case 5:
				if (getoffsetX() >= -1 - getWidthModifierAmount())
					offsetX = getoffsetX() - 1;
				break;
			case 6:
				if (getoffsetX() <= 1 + getWidthModifierAmount())
					offsetX = getoffsetX() + 1;
				break;
		}
		setChanged();
	}

	@OnlyIn(Dist.CLIENT)
	public int getProgressScaled(int count) {
		return getProgress() * count / (MAX_SPAWNING_TIME);
	}

	private boolean canOperate() {
		return hasSpawnEggItem() && hasFuel();
	}

	public boolean hasSpawnEggItem() {
		return !stack(inputSlots, 0).isEmpty() && stack(inputSlots, 0).getItem() instanceof SpawnEggItem;
	}

	private boolean hasFuel() {
		return !stack(fuelSlot, 0).isEmpty() && stack(fuelSlot, 0).getItem() == ModItems.SOLID_XP_BABY.get();
	}

	private boolean hasWidthUpgrade() {
		return !stack(inputSlots, 1).isEmpty() && stack(inputSlots, 1).getItem() == ModItems.SPAWNER_UPGRADE_WIDTH.get();
	}

	public int getWidthModifierAmount() {
		return hasWidthUpgrade() ? stack(inputSlots, 1).getCount() : 0;
	}

	private boolean hasHeightUpgrade() {
		return !stack(inputSlots, 2).isEmpty() && stack(inputSlots, 2).getItem() == ModItems.SPAWNER_UPGRADE_HEIGHT.get();
	}

	public int getHeightModifierAmount() {
		return hasHeightUpgrade() ? stack(inputSlots, 2).getCount() : 0;
	}

	private boolean hasSpeedUpgrade() {
		return !stack(inputSlots, 3).isEmpty() && stack(inputSlots, 3).getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get();
	}

	public int getSpeedModifierAmount() {
		return hasSpeedUpgrade() ? stack(inputSlots, 3).getCount() : 0;
	}

	public AABB getAABBWithModifiers() {
		double x = getBlockPos().getX() + 0.5D;
		double y = getBlockPos().getY() + 0.5D;
		double z = getBlockPos().getZ() + 0.5D;
		return new AABB(x - 1.5D - getWidthModifierAmount(), y - 0.5D - getHeightModifierAmount(), z - 1.5D - getWidthModifierAmount(), x + 1.5D + getWidthModifierAmount(), y + 0.5D + getHeightModifierAmount(), z + 1.5D + getWidthModifierAmount()).move(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@OnlyIn(Dist.CLIENT)
	public AABB getAABBForRender() {
		return new AABB(- 1D - getWidthModifierAmount(), - 0D - getHeightModifierAmount(), - 1D - getWidthModifierAmount(), 2D + getWidthModifierAmount(), 1D + getHeightModifierAmount(), 2D + getWidthModifierAmount()).move(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	public int getoffsetX() {
		return Math.max(- 2 - getWidthModifierAmount(), Math.min(offsetX, 2 + getWidthModifierAmount()));
	}

	public int getoffsetY() {
		return Math.max(- 1 - getHeightModifierAmount(), Math.min(offsetY, 1 + getHeightModifierAmount()));
	}

	public int getoffsetZ() {
		return Math.max(- 2 - getWidthModifierAmount(), Math.min(offsetZ, 2 + getWidthModifierAmount()));
	}

	private void setProgress(int counter) {
		spawning_progress = counter;
		updateBlock();
	}

	public int getProgress() {
		return spawning_progress;
	}

	@Override
	protected void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
		for (int slot = 0; slot < 4; slot++)
			setStack(inputSlots, slot, input.read("input" + slot, ItemStack.CODEC).orElse(ItemStack.EMPTY));
		setStack(fuelSlot, 0, input.read("fuel", ItemStack.CODEC).orElse(ItemStack.EMPTY));
		isOn = input.getBooleanOr("isOn", false);
		showRenderBox = input.getBooleanOr("showRenderBox", false);
		offsetX = input.getIntOr("offsetX", 0);
		offsetY = input.getIntOr("offsetY", 0);
		offsetZ = input.getIntOr("offsetZ", 0);
		spawning_progress = input.getIntOr("spawning_progress", 0);
	}

	@Override
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
		for (int slot = 0; slot < 4; slot++)
			output.store("input" + slot, ItemStack.CODEC, stack(inputSlots, slot));
		output.store("fuel", ItemStack.CODEC, stack(fuelSlot, 0));
		output.putBoolean("isOn", isOn);
		output.putBoolean("showRenderBox", showRenderBox);
		output.putInt("offsetX", offsetX);
		output.putInt("offsetY", offsetY);
		output.putInt("offsetZ", offsetZ);
		output.putInt("spawning_progress", spawning_progress);
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
	public void onDataPacket(Connection net, ValueInput input) {
		loadAdditional(input);
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.mob_grinding_utils.entity_spawner");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player) {
		return new ContainerMGUSpawner(windowID, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(worldPosition));
	}

	@OnlyIn(Dist.CLIENT)
	public Entity getEntityToRender() {
		Entity entity = null;
		if (hasSpawnEggItem()) {
			ItemStack eggStack = stack(inputSlots, 0);
			SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
			entity = eggItem.getType(eggStack).create(getLevel(), EntitySpawnReason.SPAWNER);
		}
		return entity;
	}

	@Override
	public void buttonClicked(int buttonID) {
		switch (buttonID) {
			case 0 -> toggleRenderBox();
			case 1,2,3,4,5,6 -> toggleOffset(buttonID);
		}
		updateBlock();
	}
}
