package mob_grinding_utils.tile;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileEntityMGUSpawner extends BlockEntity implements MenuProvider, BEGuiLink {

	public int spawning_progress = 0;
	public int MAX_SPAWNING_TIME = 100;
	public boolean isOn = false;

	public ItemStackHandler inputSlots = new ItemStackHandler(4);
	public ItemStackHandler fuelSlot = new ItemStackHandler(1) {
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return stack.getItem() == ModItems.SOLID_XP_BABY.get();
		}
	};

	public int animationTicks, prevAnimationTicks;
	public boolean showRenderBox;
	public int offsetX, offsetY, offsetZ;

	public TileEntityMGUSpawner(BlockPos pos, BlockState state) {
		super(ModBlocks.ENTITY_SPAWNER.getTileEntityType(), pos, state);
	}

	public IItemHandler getFuelSlot(@Nullable Direction side) {
		return fuelSlot;
	}

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
							tile.fuelSlot.getStackInSlot(0).shrink(1);
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
		ItemStack eggStack = inputSlots.getStackInSlot(0);
		SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
		type = eggItem.getType(null);

		if (type != null && !type.is(ModTags.Entities.NO_SPAWN)) {
			AABB axisalignedbb = getAABBWithModifiers();
			int minX = Mth.floor(axisalignedbb.minX);
			int maxX = Mth.floor(axisalignedbb.maxX);
			int minY = Mth.floor(axisalignedbb.minY);
			int maxY = Mth.floor(axisalignedbb.maxY);
			int minZ = Mth.floor(axisalignedbb.minZ);
			int maxZ = Mth.floor(axisalignedbb.maxZ);
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
			Mob entity = (Mob) type.create(getLevel());
			List<BlockPos> posArrayList = new ArrayList<BlockPos>();
			if (entity != null) {
				for (int x = minX; x < maxX; x++) {
					for (int y = minY; y < maxY; y++) {
						for (int z = minZ; z < maxZ; z++) {
							mutablePos.set(x, y, z);
							entity.setPos(mutablePos.getX() + 0.5D, mutablePos.getY(), mutablePos.getZ() + 0.5D);
							if (isValidSpawnLocation(getLevel(), type, entity, mutablePos)) {
								posArrayList.add(new BlockPos(mutablePos));
							}
						}
					}
				}
				if (!posArrayList.isEmpty()) {
					Collections.shuffle(posArrayList);
					entity.setPos(posArrayList.get(0).getX() + 0.5D, posArrayList.get(0).getY(), posArrayList.get(0).getZ() + 0.5D);
					entity.finalizeSpawn((ServerLevelAccessor) getLevel(), getLevel().getCurrentDifficultyAt(posArrayList.get(0)), MobSpawnType.SPAWNER, null, null);
					getLevel().addFreshEntity(entity);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidSpawnLocation(Level world, EntityType<?> type, Entity entity, BlockPos pos) {
		return NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), world, pos, type) && world.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && getLevel().noCollision(entity);
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
		return !inputSlots.getStackInSlot(0).isEmpty() && inputSlots.getStackInSlot(0).getItem() instanceof SpawnEggItem;
	}

	private boolean hasFuel() {
		return !fuelSlot.getStackInSlot(0).isEmpty() && fuelSlot.getStackInSlot(0).getItem() == ModItems.SOLID_XP_BABY.get();
	}

	private boolean hasWidthUpgrade() {
		return !inputSlots.getStackInSlot(1).isEmpty() && inputSlots.getStackInSlot(1).getItem() == ModItems.SPAWNER_UPGRADE_WIDTH.get();
	}

	public int getWidthModifierAmount() {
		return hasWidthUpgrade() ? inputSlots.getStackInSlot(1).getCount() : 0;
	}

	private boolean hasHeightUpgrade() {
		return !inputSlots.getStackInSlot(2).isEmpty() && inputSlots.getStackInSlot(2).getItem() == ModItems.SPAWNER_UPGRADE_HEIGHT.get();
	}

	public int getHeightModifierAmount() {
		return hasHeightUpgrade() ? inputSlots.getStackInSlot(2).getCount() : 0;
	}

	private boolean hasSpeedUpgrade() {
		return !inputSlots.getStackInSlot(3).isEmpty() && inputSlots.getStackInSlot(3).getItem() == ModItems.XP_SOLIDIFIER_UPGRADE.get(); //TODO temp items here
	}

	public int getSpeedModifierAmount() {
		return hasSpeedUpgrade() ? inputSlots.getStackInSlot(3).getCount() : 0;
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
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inputSlots.deserializeNBT(nbt.getCompound("inputSlots"));
		fuelSlot.deserializeNBT(nbt.getCompound("fuelSlot"));
		isOn = nbt.getBoolean("isOn");
		showRenderBox = nbt.getBoolean("showRenderBox");
		offsetX = nbt.getInt("offsetX");
		offsetY = nbt.getInt("offsetY");
		offsetZ = nbt.getInt("offsetZ");
		spawning_progress = nbt.getInt("spawning_progress");
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.put("inputSlots", inputSlots.serializeNBT());
		nbt.put("fuelSlot", fuelSlot.serializeNBT());
		nbt.putBoolean("isOn", isOn);
		nbt.putBoolean("showRenderBox", showRenderBox);
		nbt.putInt("offsetX", offsetX);
		nbt.putInt("offsetY", offsetY);
		nbt.putInt("offsetZ", offsetZ);
		nbt.putInt("spawning_progress", spawning_progress);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return nbt;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		super.onDataPacket(net, packet);
		load(packet.getTag());
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
			ItemStack eggStack = inputSlots.getStackInSlot(0);
			SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
			entity = eggItem.getType(null).create(getLevel());
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
