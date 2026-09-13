package mob_grinding_utils.BlockEntities;

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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BlockEntityXPTap extends BlockEntity {
	
	public BlockEntityXPTap(BlockPos pos, BlockState state) {
		super(ModBlocks.XP_TAP.getTileEntityType(), pos, state);
	}

	public boolean active;

	public static <T extends BlockEntity> void serverTick(Level world, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof BlockEntityXPTap blockEntityXPTap && blockEntityXPTap.active) {
			BlockPos blockPos = worldPosition.relative(world.getBlockState(worldPosition).getValue(BlockXPTap.FACING).getOpposite());
			BlockEntity tileentity = world.getBlockEntity(blockPos);
			if (tileentity != null) {
				Optional<ResourceHandler<FluidResource>> fluidHandler = CapHelper.getFluidHandler(world, blockPos, world.getBlockState(worldPosition).getValue(BlockXPTap.FACING));
				fluidHandler.ifPresent((handler) -> {
					if (handler.size() > 0 && handler.getAmountAsInt(0) >= 20 && handler.getResource(0).getFluid().is(ModTags.Fluids.EXPERIENCE) && world.getGameTime() % 3 == 0) {
						int xpAmount = EntityXPOrbFalling.getExperienceValue(Math.min(20, handler.getAmountAsInt(0) / 20));
						try (Transaction tx = Transaction.openRoot()) {
							int drained = handler.extract(0, handler.getResource(0), xpAmount * 20, tx);
							if (drained == xpAmount * 20) {
								tx.commit();
								blockEntityXPTap.spawnXP(world, worldPosition, xpAmount, tileentity);
								PacketDistributor.sendToPlayersNear((ServerLevel) world, null, t.getBlockPos().getX(), t.getBlockPos().getY(), t.getBlockPos().getZ(), 30,new TapParticlePacket(worldPosition));
							}
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
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
		output.putBoolean("active", active);
	}

	@Override
	protected void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
		active = input.getBooleanOr("active", false);
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
	public void onDataPacket(@Nonnull Connection net, @Nonnull ValueInput valueInput) {
		super.onDataPacket(net, valueInput);
	}
}
