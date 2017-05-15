package mob_grinding_utils.blocks;

import java.util.List;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockXPTap extends BlockDirectional implements ITileEntityProvider {

	protected static final AxisAlignedBB XP_TAP_WEST_AABB = new AxisAlignedBB(0.4375D, 0.5D, 0.25D, 1, 1D, 0.75);
	protected static final AxisAlignedBB XP_TAP_EAST_AABB = new AxisAlignedBB(0D, 0.5D, 0.25D, 0.5625D, 1D, 0.75D);
	protected static final AxisAlignedBB XP_TAP_SOUTH_AABB = new AxisAlignedBB(0.25D, 0.5D, 0D, 0.75D, 1D, 0.5625D);
	protected static final AxisAlignedBB XP_TAP_NORTH_AABB = new AxisAlignedBB(0.25D, 0.5D, 0.4375D, 0.75D, 1D, 1D);
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockXPTap() {
		super(Material.CIRCUITS);
		setDefaultState(this.getBlockState().getBaseState().withProperty(POWERED, false));
		setHardness(1.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = state.getActualState(source, pos);
		switch ((EnumFacing) state.getValue(FACING)) {
		default:
		case EAST:
			return XP_TAP_EAST_AABB;
		case WEST:
			return XP_TAP_WEST_AABB;
		case SOUTH:
			return XP_TAP_SOUTH_AABB;
		case NORTH:
			return XP_TAP_NORTH_AABB;
		}
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		state = state.getActualState(world, pos);
		switch ((EnumFacing) state.getValue(FACING)) {
		default:
		case EAST:
			return XP_TAP_EAST_AABB;
		case WEST:
			return XP_TAP_WEST_AABB;
		case SOUTH:
			return XP_TAP_SOUTH_AABB;
		case NORTH:
			return XP_TAP_NORTH_AABB;
		}
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean whatIsThis) {
		state = state.getActualState(worldIn, pos);
		switch ((EnumFacing) state.getValue(FACING)) {
		default:
		case EAST:
			addCollisionBoxToList(pos, entityBox, collidingBoxes, XP_TAP_EAST_AABB);
			break;
		case WEST:
			addCollisionBoxToList(pos, entityBox, collidingBoxes, XP_TAP_WEST_AABB);
			break;
		case SOUTH:
			addCollisionBoxToList(pos, entityBox, collidingBoxes, XP_TAP_SOUTH_AABB);
			break;
		case NORTH:
			addCollisionBoxToList(pos, entityBox, collidingBoxes, XP_TAP_NORTH_AABB);
			break;
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	 public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if (canPlaceAt(world, pos, facing))
			return this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, false);
		return this.getDefaultState();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			state = state.cycleProperty(POWERED);
			world.setBlockState(pos, state, 3);
			float f = ((Boolean) state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
			world.playSound((EntityPlayer) null, pos, MobGrindingUtils.TAP_SQUEAK, SoundCategory.BLOCKS, 0.3F, f);
			TileEntityXPTap tileentity = (TileEntityXPTap) world.getTileEntity(pos);
			tileentity.setActive(((Boolean) state.getValue(POWERED)).booleanValue());
			return true;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		for (EnumFacing enumfacing : FACING.getAllowedValues()) {
			if (canPlaceAt(world, pos, enumfacing))
				return true;
		}
		return false;
	}

	private boolean canPlaceAt(World world, BlockPos pos, EnumFacing facing) {
		BlockPos blockpos = pos.offset(facing.getOpposite());
		boolean isSide = facing.getAxis().isHorizontal();
		return isSide && world.getBlockState(blockpos).getBlock() instanceof BlockTank;
	}

	@Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		EnumFacing facing = world.getBlockState(pos).getValue(FACING);
    	if(!canPlaceAt((World) world, pos, facing)) {
            this.dropBlockAsItem((World) world, pos, world.getBlockState(pos), 0);
            ((World) world).setBlockToAir(pos);
        }
    }

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y)
			facing = EnumFacing.NORTH;
		return getDefaultState().withProperty(FACING, facing).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
	}

	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta = meta | ((EnumFacing) state.getValue(FACING)).getIndex();

		if (((Boolean) state.getValue(POWERED)).booleanValue())
			meta |= 8;

		return meta;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, POWERED });
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityXPTap();
	}
}
