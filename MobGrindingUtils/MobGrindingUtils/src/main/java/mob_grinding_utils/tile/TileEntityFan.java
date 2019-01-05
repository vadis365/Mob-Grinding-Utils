package mob_grinding_utils.tile;

import java.util.List;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.blocks.BlockFan;
import mob_grinding_utils.items.ItemFanUpgrade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFan extends TileEntityInventoryHelper implements ITickable {

	private static final int[] SLOTS = new int[] {0, 1, 2};
	public boolean showRenderBox;
	float xPos, yPos, zPos;
	float xNeg, yNeg, zNeg;

	public TileEntityFan() {
		super(3);
	}

	@Override
	public void update() {
		if (getWorld().getTotalWorldTime()%2==0 && getWorld().getBlockState(getPos()).getBlock() != null)
			if (getWorld().getBlockState(getPos()).getValue(BlockFan.POWERED)) {
				activateBlock();
		}
		if (!getWorld().isRemote)
			setAABBWithModifiers();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public int getWidthModifier() {
		return hasWidthUpgrade() ? getItems().get(0).getCount() : 0;
	}

	public int getHeightModifier() {
		return hasHeightUpgrade() ? getItems().get(1).getCount() : 0;
	}

	public int getSpeedModifier() {
		return hasSpeedUpgrade() ? getItems().get(2).getCount() : 0;
	}
	
	public void setAABBWithModifiers() {
		IBlockState state = getWorld().getBlockState(getPos());
		EnumFacing facing = state.getValue(BlockFan.FACING);

		int distance;
		for (distance = 1; distance < 5 + getSpeedModifier(); distance++) {
			IBlockState state2 = getWorld().getBlockState(getPos().offset(facing, distance));
			if (state2 != Blocks.AIR.getDefaultState())
				break;
		}

		if (facing == EnumFacing.UP) {
			yPos = distance;
			yNeg = -1;
			xPos = getHeightModifier();
			xNeg = getHeightModifier();
			zPos = getWidthModifier();
			zNeg = getWidthModifier();
		}
		if (facing == EnumFacing.DOWN) {
			yNeg = distance;
			yPos = -1;
			xPos = getHeightModifier();
			xNeg = getHeightModifier();
			zPos = getWidthModifier();
			zNeg = getWidthModifier();
		}
		if (facing == EnumFacing.WEST) {
			xNeg = distance;
			xPos = -1;
			zPos = getWidthModifier();
			zNeg = getWidthModifier();
			yPos = getHeightModifier();
			yNeg = getHeightModifier();
		}
		if (facing == EnumFacing.EAST) {
			xPos = distance;
			xNeg = -1;
			zPos = getWidthModifier();
			zNeg = getWidthModifier();
			yPos = getHeightModifier();
			yNeg = getHeightModifier();
		}
		if (facing == EnumFacing.NORTH) {
			zNeg = distance;
			zPos = -1;
			xPos = getWidthModifier();
			xNeg = getWidthModifier();
			yPos = getHeightModifier();
			yNeg = getHeightModifier();
		}
		if (facing == EnumFacing.SOUTH) {
			zPos = distance;
			zNeg = -1;
			xPos = getWidthModifier();
			xNeg = getWidthModifier();
			yPos = getHeightModifier();
			yNeg = getHeightModifier();
		}
		getWorld().notifyBlockUpdate(getPos(), state, state, 8);
	}

	public AxisAlignedBB getAABBWithModifiers() {
		return new AxisAlignedBB(getPos().getX() - xNeg, getPos().getY() - yNeg, getPos().getZ() - zNeg, getPos().getX() + 1D + xPos, getPos().getY() + 1D + yPos, getPos().getZ() + 1D + zPos);
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getAABBForRender() {
		return new AxisAlignedBB(- xNeg, - yNeg, - zNeg, 1D + xPos, 1D + yPos, 1D + zPos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos().getX() - xNeg, getPos().getY() - yNeg, getPos().getZ() - zNeg, getPos().getX() + 1D + xPos, getPos().getY() + 1D + yPos, getPos().getZ() + 1D + zPos);
	}

	public void toggleRenderBox() {
		if (!showRenderBox)
			showRenderBox = true;
		else
			showRenderBox = false;
		markDirty();
	}

	@SuppressWarnings("unchecked")
	protected Entity activateBlock() {
		IBlockState state = getWorld().getBlockState(getPos());
		EnumFacing facing = state.getValue(BlockFan.FACING);
		List<EntityLivingBase> list = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, getAABBWithModifiers());
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
		return !getItems().get(0).isEmpty() && getItems().get(0).getItem() == ModItems.FAN_UPGRADE && getItems().get(0).getItemDamage() == 0;
	}

	private boolean hasHeightUpgrade() {
		return !getItems().get(1).isEmpty() && getItems().get(1).getItem() == ModItems.FAN_UPGRADE && getItems().get(1).getItemDamage() == 1;
	}

	private boolean hasSpeedUpgrade() {
		return !getItems().get(2).isEmpty() && getItems().get(2).getItem() == ModItems.FAN_UPGRADE && getItems().get(2).getItemDamage() == 2;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("showRenderBox", showRenderBox);
		nbt.setFloat("xPos", xPos);
		nbt.setFloat("yPos", yPos);
		nbt.setFloat("zPos", zPos);
		nbt.setFloat("xNeg", xNeg);
		nbt.setFloat("yNeg", yNeg);
		nbt.setFloat("zNeg", zNeg);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		showRenderBox = nbt.getBoolean("showRenderBox");
		xPos = nbt.getFloat("xPos");
		yPos = nbt.getFloat("yPos");
		zPos = nbt.getFloat("zPos");
		xNeg = nbt.getFloat("xNeg");
		yNeg = nbt.getFloat("yNeg");
		zNeg = nbt.getFloat("zNeg");
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
		return new SPacketUpdateTileEntity(getPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		onContentsChanged();
	}

	public void onContentsChanged() {
		if (this != null && !getWorld().isRemote) {
			final IBlockState state = getWorld().getBlockState(getPos());
			setAABBWithModifiers();
			getWorld().notifyBlockUpdate(getPos(), state, state, 8);
			markDirty();
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemFanUpgrade;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(getItems(), index);
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
