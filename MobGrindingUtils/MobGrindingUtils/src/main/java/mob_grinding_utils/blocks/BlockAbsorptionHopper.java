package mob_grinding_utils.blocks;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockAbsorptionHopper extends BlockContainer {

	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> NORTH = EnumProperty.create("north", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> SOUTH = EnumProperty.create("south", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> WEST = EnumProperty.create("west", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> EAST = EnumProperty.create("east", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> UP = EnumProperty.create("up", TileEntityAbsorptionHopper.EnumStatus.class);
	public static final EnumProperty<TileEntityAbsorptionHopper.EnumStatus> DOWN = EnumProperty.create("down", TileEntityAbsorptionHopper.EnumStatus.class);
	private static final VoxelShape HOPPER_AABB = Block.makeCuboidShape(4, 4, 4, 12, 12, 12);

	public BlockAbsorptionHopper() {
		super(Builder.create(Material.IRON).hardnessAndResistance(10.0F, 2000.0F));
		//setHardness(10.0F);
		//setResistance(2000.0F);
		//setHarvestLevel("pickaxe", 0);
		//setSoundType(SoundType.METAL);
		//setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityAbsorptionHopper();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return HOPPER_AABB;
	}

	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return HOPPER_AABB;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	   public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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

	@SuppressWarnings("deprecation")
	public IItemProvider getItemDropped(IBlockState state, World world, BlockPos pos, int fortune) {
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
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onTextureStitchPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getStill());
		event.getMap().registerSprite(MobGrindingUtils.FLUID_XP.getFlowing());
	}
}
