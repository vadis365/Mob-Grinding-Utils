package mob_grinding_utils.tile;

import java.util.List;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.blocks.BlockFan;
import mob_grinding_utils.items.ItemFanUpgrade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityFan extends TileEntityInventoryHelper implements ITickable {

	public boolean active;
	private static final int[] SLOTS = new int[] {0, 1, 2};

	public TileEntityFan() {
		super(3);
	}

	@Override
	public void update() {
		if (getWorld().getWorldTime()%2==0 && getWorld().getBlockState(pos).getBlock() != null)
			if (getWorld().getBlockState(pos).getValue(BlockFan.POWERED)) {
				activateBlock();
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}

	@SuppressWarnings("unchecked")
	protected Entity activateBlock() {
		IBlockState state = getWorld().getBlockState(pos);
		EnumFacing facing = state.getValue(BlockFan.FACING);
		float xPos = 0, yPos = 0 , zPos = 0;
		float xNeg = 0, yNeg = 0 , zNeg = 0;
		int widthMod = 0;
		int heightMod = 0;
		int speedMod = 0;
		int distance;
		if(hasWidthUpgrade())
			widthMod = getItems().get(0).getCount();
		if(hasHeightUpgrade())
			heightMod = getItems().get(1).getCount();
		if(hasSpeedUpgrade()) {
			speedMod = getItems().get(2).getCount();
			if(speedMod > 10)
				speedMod = 10;
		}
		for(distance = 1; distance < 5 + speedMod; distance++) {
			IBlockState state2 = getWorld().getBlockState(pos.offset(facing, distance));
			if(state2 != Blocks.AIR.getDefaultState())
				break;
		}

		if(facing == EnumFacing.UP) {
			yPos = distance;
			yNeg = - 1;
			xPos = heightMod;
			xNeg = heightMod;
			zPos = widthMod;
			zNeg = widthMod;
		}
		if(facing == EnumFacing.DOWN) {
			yNeg = distance;
			yPos =  - 1;
			xPos = heightMod;
			xNeg = heightMod;
			zPos = widthMod;
			zNeg = widthMod;
		}
		if(facing == EnumFacing.WEST) {
			xNeg = distance;
			xPos = - 1;
			zPos = widthMod;
			zNeg = widthMod;
			yPos = heightMod;
			yNeg = heightMod;
		}
		if(facing == EnumFacing.EAST) {
			xPos = distance;
			xNeg = - 1;
			zPos = widthMod;
			zNeg = widthMod;
			yPos = heightMod;
			yNeg = heightMod;
		}
		if(facing == EnumFacing.NORTH) {
			zNeg = distance;
			zPos = - 1;
			xPos = widthMod;
			xNeg = widthMod;
			yPos = heightMod;
			yNeg = heightMod;
		}
		if(facing == EnumFacing.SOUTH) {
			zPos = distance;
			zNeg = - 1;
			xPos = widthMod;
			xNeg = widthMod;
			yPos = heightMod;
			yNeg = heightMod;
		}
		List<EntityLivingBase> list = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX() - xNeg, pos.getY() - yNeg, pos.getZ() - zNeg, pos.getX() + 1D  + xPos, pos.getY() + 1D  + yPos, pos.getZ() + 1D  + zPos));
		for (int i = 0; i < list.size(); i++) {
			Entity entity = list.get(i);
			if (entity != null) {
				if (entity instanceof EntityLivingBase) {
					if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
						entity.addVelocity(MathHelper.sin(facing.getOpposite().getHorizontalAngle() * 3.141593F / 180.0F) * 0.5D, 0D, -MathHelper.cos(facing.getOpposite().getHorizontalAngle() * 3.141593F / 180.0F) * 0.5D);
					} else if (facing == EnumFacing.UP) {
						entity.motionY += 0.125D;
						entity.addVelocity(0D, 0.25D, 0D);
						entity.fallDistance = 0;
					} else if (facing == EnumFacing.DOWN)
						entity.addVelocity(0D, -0.2D, 0D);
				}
			}
		}
		return null;
	}

	private boolean hasWidthUpgrade() {
		return !getItems().get(0).isEmpty() && getItems().get(0).getItem() == MobGrindingUtils.FAN_UPGRADE && getItems().get(0).getItemDamage() == 0;
	}
	
	private boolean hasHeightUpgrade() {
		return !getItems().get(1).isEmpty() && getItems().get(1).getItem() == MobGrindingUtils.FAN_UPGRADE && getItems().get(1).getItemDamage() == 1;
	}

	private boolean hasSpeedUpgrade() {
		return !getItems().get(2).isEmpty() && getItems().get(2).getItem() == MobGrindingUtils.FAN_UPGRADE && getItems().get(2).getItemDamage() == 2;
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
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemFanUpgrade;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

}
