package mob_grinding_utils.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.inventory.server.ContainerMGUSpawner;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMGUSpawner extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

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
    private final LazyOptional<IItemHandler> fuelSlotCap = LazyOptional.of(() -> fuelSlot);

	public int animationTicks, prevAnimationTicks;
	public boolean showRenderBox;
	public int offsetX, offsetY, offsetZ;

    public TileEntityMGUSpawner() {
        super(ModBlocks.ENTITY_SPAWNER.getTileEntityType());
    }

	public void toggleOnOff() {
		isOn = !isOn;
	}

    @Override
    public void tick() {
    	if(isOn) {
			if (getWorld().isRemote) {
				prevAnimationTicks = animationTicks;
				if (animationTicks < 360)
					animationTicks += 9;
				if (animationTicks >= 360) {
					animationTicks -= 360;
					prevAnimationTicks -= 360;
				}
			}
			if (!getWorld().isRemote) {
				if (canOperate()) {
					setProgress(getProgress() + 1 + getSpeedModifierAmount());
					if (getProgress() >= MAX_SPAWNING_TIME) {
						if (spawnMobInArea())
							fuelSlot.getStackInSlot(0).shrink(1);
						setProgress(0);
						return;
					}
				} else {
					if (getProgress() > 0)
						setProgress(0);
				}
			}
		}
		else {
			if (getWorld().isRemote)
				prevAnimationTicks = animationTicks = 0;

			if (!getWorld().isRemote)
				if (getProgress() > 0)
					setProgress(0);
		}
    }

	private boolean spawnMobInArea() {
			EntityType<?> type = null;
			ItemStack eggStack = inputSlots.getStackInSlot(0);
			SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
			type = eggItem.getType(null);

			if (type != null) {
				AxisAlignedBB axisalignedbb = getAABBWithModifiers();
				int minX = MathHelper.floor(axisalignedbb.minX);
				int maxX = MathHelper.floor(axisalignedbb.maxX);
				int minY = MathHelper.floor(axisalignedbb.minY);
				int maxY = MathHelper.floor(axisalignedbb.maxY);
				int minZ = MathHelper.floor(axisalignedbb.minZ);
				int maxZ = MathHelper.floor(axisalignedbb.maxZ);
				BlockPos.Mutable mutablePos = new BlockPos.Mutable();
				MobEntity entity = (MobEntity) type.create(getWorld());
				List<BlockPos> posArrayList = new ArrayList<BlockPos>();
			if (entity != null) {
				for (int x = minX; x < maxX; x++) {
					for (int y = minY; y < maxY; y++) {
						for (int z = minZ; z < maxZ; z++) {
							BlockPos posList = new BlockPos(x, y, z);
							entity.setPosition(posList.getX() + 0.5D, posList.getY(), posList.getZ() + 0.5D);
							if (isValidSpawnLocation(getWorld(), type, entity, posList)) {
								posArrayList.add(posList);
							}
						}
					}
				}
				if (!posArrayList.isEmpty()) {
					Collections.shuffle(posArrayList);
					entity.setPosition(posArrayList.get(0).getX() + 0.5D, posArrayList.get(0).getY(), posArrayList.get(0).getZ() + 0.5D);
					entity.onInitialSpawn((IServerWorld) getWorld(), getWorld().getDifficultyForLocation(posArrayList.get(0)), SpawnReason.NATURAL, null, null);
					getWorld().addEntity(entity);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidSpawnLocation(World world, EntityType<?> type, Entity entity, BlockPos pos) {
		return WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementType(type), world, pos, type) && world.getEntitiesWithinAABB(entity.getType(), entity.getBoundingBox(), EntityPredicates.IS_ALIVE).isEmpty() && getWorld().hasNoCollisions(entity);
	}

	public void toggleRenderBox() {
		showRenderBox = !showRenderBox;
		markDirty();
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
		markDirty();
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

	public AxisAlignedBB getAABBWithModifiers() {
		double x = getPos().getX() + 0.5D;
		double y = getPos().getY() + 0.5D;
		double z = getPos().getZ() + 0.5D;
		return new AxisAlignedBB(x - 1.5D - getWidthModifierAmount(), y - 0.5D - getHeightModifierAmount(), z - 1.5D - getWidthModifierAmount(), x + 1.5D + getWidthModifierAmount(), y + 0.5D + getHeightModifierAmount(), z + 1.5D + getWidthModifierAmount()).offset(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getAABBForRender() {
		return new AxisAlignedBB(- 1D - getWidthModifierAmount(), - 0D - getHeightModifierAmount(), - 1D - getWidthModifierAmount(), 2D + getWidthModifierAmount(), 1D + getHeightModifierAmount(), 2D + getWidthModifierAmount()).offset(getoffsetX(), getoffsetY(), getoffsetZ());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return getAABBWithModifiers();
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
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
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
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("inputSlots", inputSlots.serializeNBT());
        nbt.put("fuelSlot", fuelSlot.serializeNBT());
        nbt.putBoolean("isOn", isOn);
        nbt.putBoolean("showRenderBox", showRenderBox);
        nbt.putInt("offsetX", offsetX);
        nbt.putInt("offsetY", offsetY);
        nbt.putInt("offsetZ", offsetZ);
        nbt.putInt("spawning_progress", spawning_progress);
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
    }

    public void updateBlock() {
        getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("block.mob_grinding_utils.entity_spawner");
    }

    @Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerMGUSpawner(windowID, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
    }

	@OnlyIn(Dist.CLIENT)
	public Entity getEntityToRender() {
		Entity entity = null;
		if (hasSpawnEggItem()) {
			ItemStack eggStack = inputSlots.getStackInSlot(0);
			SpawnEggItem eggItem = (SpawnEggItem) eggStack.getItem();
			entity = eggItem.getType(null).create(getWorld());
		}
		return entity;
	}

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return fuelSlotCap.cast();
        return super.getCapability(cap, side);
    }

}
