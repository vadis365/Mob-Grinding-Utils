package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockAbsorptionHopper extends ContainerBlock {

	public static final VoxelShape HOPPER_AABB = Block.makeCuboidShape(4D, 4D, 4D, 12D, 12D, 12D);

	public BlockAbsorptionHopper(Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityAbsorptionHopper();
	}

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
				} else {
					vacuum.toggleMode(hit.getFace());
					world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
					world.notifyBlockUpdate(pos, state, state, 3);
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

/* TODO
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
}
