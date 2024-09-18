package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import mob_grinding_utils.network.TapParticlePacket;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TileEntityXPTap extends BlockEntity {
	
	public TileEntityXPTap(BlockPos pos, BlockState state) {
		super(ModBlocks.XP_TAP.getTileEntityType(), pos, state);
	}

	public boolean active;

	public static <T extends BlockEntity> void serverTick(Level world, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof TileEntityXPTap tileEntityXPTap && tileEntityXPTap.active) {
			BlockPos blockPos = worldPosition.relative(world.getBlockState(worldPosition).getValue(BlockXPTap.FACING).getOpposite());
			BlockEntity tileentity = world.getBlockEntity(blockPos);
			if (tileentity != null) {
				Optional<IFluidHandler> fluidHandler = CapHelper.getFluidHandler(world, blockPos, world.getBlockState(worldPosition).getValue(BlockXPTap.FACING));
				fluidHandler.ifPresent((handler) -> {
					if (handler.getTanks() > 0 && handler.getFluidInTank(0).getAmount() >= 20 && handler.getFluidInTank(0).getFluid().is(ModTags.Fluids.EXPERIENCE) && world.getGameTime() % 3 == 0) {
						int xpAmount = EntityXPOrbFalling.getExperienceValue(Math.min(20, handler.getFluidInTank(0).getAmount() / 20));
						if (!handler.drain(xpAmount * 20, IFluidHandler.FluidAction.EXECUTE).isEmpty()) {
							tileEntityXPTap.spawnXP(world, worldPosition, xpAmount, tileentity);
							PacketDistributor.sendToPlayersNear((ServerLevel) world, null, t.getBlockPos().getX(), t.getBlockPos().getY(), t.getBlockPos().getZ(), 30,new TapParticlePacket(worldPosition));
						}
					}
				});
			}
		}
	}

	public void spawnXP(Level world, BlockPos pos, int xp, BlockEntity tankTile) {
		tankTile.setChanged();
		EntityXPOrbFalling orb = new EntityXPOrbFalling(world, pos.getX() + 0.5D, pos.getY() - 0.125D, pos.getZ() + 0.5D, xp);
		world.addFreshEntity(orb);
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		nbt.putBoolean("active", active);
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		active = nbt.getBoolean("active");
	}

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
	public void onDataPacket(@Nonnull Connection net, ClientboundBlockEntityDataPacket packet, @Nonnull HolderLookup.Provider registries) {
		if (packet.getTag() != null)
			loadAdditional(packet.getTag(), registries);
	}
}
