package mob_grinding_utils.tile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import mob_grinding_utils.network.TapParticleMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityXPTap extends TileEntity implements ITickable{
	public boolean active;

	@Override
	public void update() {
		if (!worldObj.isRemote && active) {
			TileEntity tileentity = worldObj.getTileEntity(pos.offset(worldObj.getBlockState(pos).getValue(BlockXPTap.FACING).getOpposite()));
			if (tileentity instanceof TileEntityTank) {
				if (((TileEntityTank) tileentity).tank.getFluidAmount() > 0 && ((TileEntityTank) tileentity).tank.getFluid().getFluid().equals(FluidRegistry.getFluid("xpjuice")) && worldObj.getTotalWorldTime() % 3 == 0) {
					int xpAmount = EntityXPOrbFalling.getXPSplit(Math.min(20, ((TileEntityTank) tileentity).tank.getFluidAmount() / 20));
					((TileEntityTank) tileentity).tank.drain(xpAmount * 20, true);
					spawnXP(worldObj, pos, xpAmount, (TileEntityTank) tileentity);
					MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new TapParticleMessage(getPos()));
				}
			}
		}
	}

	public void spawnXP(World world, BlockPos pos, int xp, TileEntityTank tankTile) {
		tankTile.markDirty();
		EntityXPOrbFalling orb = new EntityXPOrbFalling(world, pos.getX() + 0.5D, pos.getY() - 0.125D, pos.getZ() + 0.5D, xp);
		world.spawnEntityInWorld(orb);
	}

	public void setActive(boolean isActive) {
		active = isActive;
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("active", active);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		active = nbt.getBoolean("active");
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
        return writeToNBT(nbt);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new SPacketUpdateTileEntity(pos, 0, nbt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}
}
