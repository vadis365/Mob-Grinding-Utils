package mob_grinding_utils.blocks;

import java.util.Locale;
import java.util.Random;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnderInhibitorOn extends Block {

	public static final PropertyEnum<EnumGemDirection> TYPE = PropertyEnum.create("type", EnumGemDirection.class);

	public BlockEnderInhibitorOn() {
		super(Material.GLASS);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumGemDirection.DOWN_NORTH));
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {

		for (int i = 0; i < 4; ++i) {
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			int j = rand.nextInt(2) * 2 - 1;

			if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
				d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
				d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
			} else {
				d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
				d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
			}

			world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {

		float widthMin = 0, heightMin = 0, depthMin = 0;
		float widthMax = 0, heightMax = 0, depthMax = 0;
		switch (state.getValue(TYPE)) {

			case SOUTH:
				widthMin = 0.125F;
				heightMin = 0.1875F;
				depthMin = 0.8125F;
				widthMax = 0.125F;
				heightMax = 0.1875F;
				depthMax = 0F;
				break;
			case NORTH:
				widthMin = 0.125F;
				heightMin = 0.1875F;
				depthMin = 0F;
				widthMax = 0.125F;
				heightMax = 0.1875F;
				depthMax = 0.8125F;
				break;
			case EAST:
				widthMin = 0.8125F;
				heightMin = 0.1875F;
				depthMin = 0.125F;
				widthMax = 0F;
				heightMax = 0.1875F;
				depthMax = 0.125F;
				break;
			case WEST:
				widthMin = 0F;
				heightMin = 0.1875F;
				depthMin = 0.125F;
				widthMax = 0.8125F;
				heightMax = 0.1875F;
				depthMax = 0.125F;
				break;
			case UP_NORTH:
				widthMin = 0.125F;
				heightMin = 0.8125F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0F;
				depthMax = 0.1875F;
				break;
			case UP_EAST:
				widthMin = 0.1875F;
				heightMin = 0.8125F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0F;
				depthMax = 0.125F;
				break;
			case UP_SOUTH:
				widthMin = 0.125F;
				heightMin = 0.8125F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0F;
				depthMax = 0.1875F;
				break;
			case UP_WEST:
				widthMin = 0.1875F;
				heightMin = 0.8125F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0F;
				depthMax = 0.125F;
				break;
			case DOWN_NORTH:
				widthMin = 0.125F;
				heightMin = 0F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0.8125F;
				depthMax = 0.1875F;
				break;
			case DOWN_EAST:
				widthMin = 0.1875F;
				heightMin = 0F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0.8125F;
				depthMax = 0.125F;
				break;
			case DOWN_SOUTH:
				widthMin = 0.125F;
				heightMin = 0F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0.8125F;
				depthMax = 0.1875F;
				break;
			case DOWN_WEST:
				widthMin = 0.1875F;
				heightMin = 0F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0.8125F;
				depthMax = 0.125F;
				break;
		}
		return new AxisAlignedBB(0F + widthMin, 0F + heightMin, 0F + depthMin, 1F - widthMax, 1F - heightMax, 1F - depthMax);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		EnumGemDirection newFacing = state.getValue(TYPE);
		boolean flag = false;

		if (newFacing == EnumGemDirection.UP_NORTH || newFacing == EnumGemDirection.UP_EAST || newFacing == EnumGemDirection.UP_SOUTH || newFacing == EnumGemDirection.UP_WEST)
			if (world.isSideSolid(pos.up(), EnumFacing.DOWN))
				flag = true;

		if (newFacing == EnumGemDirection.DOWN_NORTH || newFacing == EnumGemDirection.DOWN_EAST || newFacing == EnumGemDirection.DOWN_SOUTH || newFacing == EnumGemDirection.DOWN_WEST)
			if (world.isSideSolid(pos.down(), EnumFacing.UP))
				flag = true;

		if (newFacing == EnumGemDirection.NORTH && world.isSideSolid(pos.offset(EnumFacing.NORTH), EnumFacing.NORTH))
			flag = true;

		if (newFacing == EnumGemDirection.SOUTH && world.isSideSolid(pos.offset(EnumFacing.SOUTH), EnumFacing.SOUTH))
			flag = true;

		if (newFacing == EnumGemDirection.WEST && world.isSideSolid(pos.offset(EnumFacing.WEST), EnumFacing.WEST))
			flag = true;

		if (newFacing == EnumGemDirection.EAST && world.isSideSolid(pos.offset(EnumFacing.EAST), EnumFacing.EAST))
			flag = true;

		if (!flag) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}

		super.neighborChanged(state, world, pos, block, fromPos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			IBlockState activeState = mob_grinding_utils.ModBlocks.ENDER_INHIBITOR_OFF.getDefaultState().withProperty(BlockEnderInhibitorOff.TYPE, state.getValue(TYPE));
			world.setBlockState(pos, activeState, 3);
			world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
			return true;
		}
	}

	@Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        return canPlaceBlock(world, pos, side);
    }

	@Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.values())
            if (canPlaceBlock(world, pos, enumfacing))
                return true;
        return false;
    }

    protected static boolean canPlaceBlock(World world, BlockPos pos, EnumFacing direction) {
        BlockPos blockpos = pos.offset(direction.getOpposite());
        IBlockState iblockstate = world.getBlockState(blockpos);
        boolean flag = iblockstate.getBlockFaceShape(world, blockpos, direction) == BlockFaceShape.SOLID;
        Block block = iblockstate.getBlock();

        return world.isBlockNormalCube(blockpos, true) && block.isOpaqueCube(iblockstate) && flag;
    }

	@SuppressWarnings("incomplete-switch")
	@Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

		int direction = MathHelper.floor(placer.rotationYaw * 4F / 360F + 0.5D) & 3;

		EnumGemDirection newFacing = EnumGemDirection.DOWN_NORTH;
		if (facing == EnumFacing.UP) {
			switch (direction) {
				case 0:
					newFacing = EnumGemDirection.DOWN_SOUTH;
					break;
				case 1:
					newFacing = EnumGemDirection.DOWN_EAST;
					break;
				case 2:
					newFacing = EnumGemDirection.DOWN_NORTH;
					break;
				case 3:
					newFacing = EnumGemDirection.DOWN_WEST;
					break;
			}
		}
		else if (facing == EnumFacing.DOWN) {
			switch (direction) {
			case 0:
				newFacing = EnumGemDirection.UP_SOUTH;
				break;
			case 1:
				newFacing = EnumGemDirection.UP_EAST;
				break;
			case 2:
				newFacing = EnumGemDirection.UP_NORTH;
				break;
			case 3:
				newFacing = EnumGemDirection.UP_WEST;
				break;
			}
		}
		else {
			switch (facing) {
			case SOUTH:
				newFacing = EnumGemDirection.NORTH;
				break;
			case EAST:
				newFacing = EnumGemDirection.WEST;
				break;
			case NORTH:
				newFacing = EnumGemDirection.SOUTH;
				break;
			case WEST:
				newFacing = EnumGemDirection.EAST;
				break;
			}
		}

		return getDefaultState().withProperty(TYPE, newFacing);
    }

	@Override
	public int damageDropped(IBlockState state) {
		return EnumGemDirection.DOWN_NORTH.ordinal();
	}

	@Override
	public int quantityDropped(Random rand) {
		return 1;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { TYPE });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab == MobGrindingUtils.TAB)
			list.add(new ItemStack(this, 1, EnumGemDirection.DOWN_NORTH.ordinal()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, EnumGemDirection.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumGemDirection type = state.getValue(TYPE);
		return type.ordinal();
	}

	public enum EnumGemDirection implements IStringSerializable {
		DOWN_NORTH,
		DOWN_SOUTH,
		DOWN_WEST,
		DOWN_EAST,
		UP_NORTH,
		UP_SOUTH,
		UP_WEST,
		UP_EAST,
		NORTH,
		SOUTH,
		WEST,
		EAST;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}
}