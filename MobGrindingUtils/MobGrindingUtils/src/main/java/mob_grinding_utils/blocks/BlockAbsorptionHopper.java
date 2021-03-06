package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import javafx.geometry.Side;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockAbsorptionHopper extends ContainerBlock {

	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> NORTH = EnumProperty.create("north", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> SOUTH = EnumProperty.create("south", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> WEST = EnumProperty.create("west", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> EAST = EnumProperty.create("east", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> UP = EnumProperty.create("up", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> DOWN = EnumProperty.create("down", TileEntityAbsorptionHopper.EnumStatus.class);
	protected static final AxisAlignedBB HOPPER_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);

	public BlockAbsorptionHopper(Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityAbsorptionHopper();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public static boolean shouldSideBeRendered(BlockState adjacentState, IBlockReader blockState, BlockPos blockAccess, Direction pos) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		return HOPPER_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean whatIsThis) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, HOPPER_AABB);
	}
/* this is defined in the clientsetup event i think... see: RenderTypeLookup.setRenderLayer
	@Override
	@OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.SOLID;
	}
*/
	/* these should be in properties
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
*/
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
      return !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
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

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event) {
		//event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getStill()); //todo
		//event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getFlowing()); //todo
	}
}
