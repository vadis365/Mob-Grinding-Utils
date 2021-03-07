package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockAbsorptionHopper extends ContainerBlock {

	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> NORTH = EnumProperty.create("north", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> SOUTH = EnumProperty.create("south", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> WEST = EnumProperty.create("west", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> EAST = EnumProperty.create("east", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> UP = EnumProperty.create("up", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> DOWN = EnumProperty.create("down", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final VoxelShape HOPPER_AABB = Block.makeCuboidShape(4D, 4D, 4D, 12D, 12D, 12D);

	public BlockAbsorptionHopper(Block.Properties properties) {
		super(properties);
		
		// this probably won't work at all - will fix later
		setDefaultState(this.stateContainer.getBaseState().with(NORTH, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
		.with(SOUTH, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
		.with(WEST, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
		.with(EAST, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
		.with(UP, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE)
		.with(DOWN, TileEntityAbsorptionHopper.EnumStatus.STATUS_NONE));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityAbsorptionHopper();
	}
/*
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
		return true;
	}
*/
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return HOPPER_AABB;
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return HOPPER_AABB;
	}
/*
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
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
*/
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileEntityAbsorptionHopper) {
				TileEntityAbsorptionHopper vacuum = (TileEntityAbsorptionHopper) tile;

				if (!player.isSneaking()) {
					world.notifyBlockUpdate(pos, state, state, 3);
					NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityAbsorptionHopper)vacuum, pos);
				} else
					vacuum.toggleMode(hit.getFace());
				BlockState newState = getActualState(vacuum, state, world, pos);
				world.setBlockState(pos, newState, 3);
			}
		}
		return ActionResultType.SUCCESS;
	}

	public BlockState getActualState(TileEntityAbsorptionHopper tile, BlockState state, World world, BlockPos pos) { //// TODO HHHNNNGGGGNNNNGGGGHHHHHNNN!
			TileEntityAbsorptionHopper.EnumStatus north = tile.getSideStatus(Direction.NORTH);
			TileEntityAbsorptionHopper.EnumStatus south = tile.getSideStatus(Direction.SOUTH);
			TileEntityAbsorptionHopper.EnumStatus west = tile.getSideStatus(Direction.WEST);
			TileEntityAbsorptionHopper.EnumStatus east = tile.getSideStatus(Direction.EAST);
			TileEntityAbsorptionHopper.EnumStatus up = tile.getSideStatus(Direction.UP);
			TileEntityAbsorptionHopper.EnumStatus down = tile.getSideStatus(Direction.DOWN);
			return state.with(NORTH, north).with(SOUTH, south).with(WEST, west)
					.with(EAST, east).with(UP, up).with(DOWN, down);
	}

/* TODO Tags
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
      return !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
    }

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}
*/

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote) {
			TileEntityAbsorptionHopper tile = (TileEntityAbsorptionHopper) world.getTileEntity(pos);
			if (tile != null) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityAbsorptionHopper) tile);
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event) {
		//event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getStill()); //todo
		//event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getFlowing()); //todo
	}
}
