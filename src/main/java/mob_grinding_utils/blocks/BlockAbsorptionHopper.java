package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAbsorptionHopper extends BlockContainer {

	public static final PropertyEnum<TileEntityAbsorptionHopper.EnumStatus> NORTH = PropertyEnum.create("north", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final PropertyEnum<TileEntityAbsorptionHopper.EnumStatus> SOUTH = PropertyEnum.create("south", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final PropertyEnum<TileEntityAbsorptionHopper.EnumStatus> WEST = PropertyEnum.create("west", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final PropertyEnum<TileEntityAbsorptionHopper.EnumStatus> EAST = PropertyEnum.create("east", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final PropertyEnum<TileEntityAbsorptionHopper.EnumStatus> UP = PropertyEnum.create("up", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final PropertyEnum<TileEntityAbsorptionHopper.EnumStatus> DOWN = PropertyEnum.create("down", TileEntityAbsorptionHopper.EnumStatus.class);
	protected static final AxisAlignedBB HOPPER_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);

	public BlockAbsorptionHopper() {
		super(Material.IRON);
		setHardness(10.0F);
		setHarvestLevel("pickaxe", 0);
		setSoundType(SoundType.METAL);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAbsorptionHopper();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		return HOPPER_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, HOPPER_AABB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileEntityAbsorptionHopper) {
				TileEntityAbsorptionHopper vacuum = (TileEntityAbsorptionHopper) tile;

				if (!player.isSneaking()) {
					world.notifyBlockUpdate(pos, state, state, 3);
					player.openGui(MobGrindingUtils.INSTANCE, MobGrindingUtils.PROXY.GUI_ID_ABSORPTION_HOPPER, world, pos.getX(), pos.getY(), pos.getZ());
				} else
					vacuum.toggleMode(side);

				world.notifyBlockUpdate(pos, state, state, 3);
			}
		}
		return true;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityAbsorptionHopper) {
			TileEntityAbsorptionHopper tile = (TileEntityAbsorptionHopper) tileEntity;
			TileEntityAbsorptionHopper.EnumStatus north = tile.getSideStatus(EnumFacing.NORTH);
			TileEntityAbsorptionHopper.EnumStatus south = tile.getSideStatus(EnumFacing.SOUTH);
			TileEntityAbsorptionHopper.EnumStatus west = tile.getSideStatus(EnumFacing.WEST);
			TileEntityAbsorptionHopper.EnumStatus east = tile.getSideStatus(EnumFacing.EAST);
			TileEntityAbsorptionHopper.EnumStatus up = tile.getSideStatus(EnumFacing.UP);
			TileEntityAbsorptionHopper.EnumStatus down = tile.getSideStatus(EnumFacing.DOWN);
			return state.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west)
					.withProperty(EAST, east).withProperty(UP, up).withProperty(DOWN, down);
		}
		return state.withProperty(NORTH, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
				.withProperty(SOUTH, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
				.withProperty(WEST, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
				.withProperty(EAST, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
				.withProperty(UP, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
				.withProperty(DOWN, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE);
	}

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntityAbsorptionHopper tile = (TileEntityAbsorptionHopper) world.getTileEntity(pos);
			if (tile != null) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityAbsorptionHopper) tile);
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getStill());
		event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getFlowing());
	}
}
