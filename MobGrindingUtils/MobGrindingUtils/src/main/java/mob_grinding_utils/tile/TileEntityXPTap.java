package mob_grinding_utils.tile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import mob_grinding_utils.network.MessageTapParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.network.PacketDistributor;

public class TileEntityXPTap extends BlockEntity {
	
	public TileEntityXPTap(BlockPos pos, BlockState state) {
		super(ModBlocks.XP_TAP.getTileEntityType(), pos, state);
	}

	public boolean active;

	public static <T extends BlockEntity> void serverTick(Level world, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof TileEntityXPTap tileEntityXPTap && tileEntityXPTap.active) {
			BlockEntity tileentity = world.getBlockEntity(worldPosition.relative(world.getBlockState(worldPosition).getValue(BlockXPTap.FACING).getOpposite()));
			if (tileentity != null) {
				LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, world.getBlockState(worldPosition).getValue(BlockXPTap.FACING));
				fluidHandler.ifPresent((handler) -> {
					if (handler.getTanks() > 0 && handler.getFluidInTank(0).getAmount() >= 20 && handler.getFluidInTank(0).getFluid().is(ModTags.Fluids.EXPERIENCE) && world.getGameTime() % 3 == 0) {
						int xpAmount = EntityXPOrbFalling.getExperienceValue(Math.min(20, handler.getFluidInTank(0).getAmount() / 20));
						if (!handler.drain(xpAmount * 20, FluidAction.EXECUTE).isEmpty()) {
							tileEntityXPTap.spawnXP(world, worldPosition, xpAmount, tileentity);
							var particleTarget = new PacketDistributor.TargetPoint(t.getBlockPos().getX(), t.getBlockPos().getY(), t.getBlockPos().getZ(), 30, t.getLevel().dimension());
							MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.NEAR.with(() -> particleTarget), new MessageTapParticle(worldPosition));
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
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putBoolean("active", active);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		active = nbt.getBoolean("active");
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
		if (packet.getTag() != null)
			load(packet.getTag());
	}
}
