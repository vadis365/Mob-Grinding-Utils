package mob_grinding_utils.tile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import mob_grinding_utils.network.MessageTapParticle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntityXPTap extends BlockEntity implements TickableBlockEntity {
	
	public TileEntityXPTap() {
		super(ModBlocks.XP_TAP.getTileEntityType());
	}

	public boolean active;

	@Override
	public void tick() {
		if (!getLevel().isClientSide && active) {
			BlockEntity tileentity = getLevel().getBlockEntity(worldPosition.relative(getLevel().getBlockState(worldPosition).getValue(BlockXPTap.FACING).getOpposite()));
			if (tileentity != null) {
				LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getLevel().getBlockState(worldPosition).getValue(BlockXPTap.FACING));
				fluidHandler.ifPresent((handler) -> {
					if (handler.getFluidInTank(0).getAmount() >= 20 && handler.getFluidInTank(0).getFluid().is(MobGrindingUtils.EXPERIENCE) && getLevel().getGameTime() % 3 == 0) {
						int xpAmount = EntityXPOrbFalling.getExperienceValue(Math.min(20, handler.getFluidInTank(0).getAmount() / 20));
						if (!handler.drain(xpAmount * 20, FluidAction.EXECUTE).isEmpty()) {
							spawnXP(getLevel(), worldPosition, xpAmount, tileentity);
							MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageTapParticle(getBlockPos()));
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
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putBoolean("active", active);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		active = nbt.getBoolean("active");
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
	}
}
