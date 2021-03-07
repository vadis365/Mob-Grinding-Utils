package mob_grinding_utils.blocks;

import java.util.Random;

import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

@SuppressWarnings("deprecation")
public class BlockFan extends DirectionalBlock implements ITileEntityProvider {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public BlockFan(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
/*
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return true;
	}
*/
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityFan();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		return this.getDefaultState().with(FACING, direction).with(POWERED, false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		else {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity  instanceof TileEntityFan)
			NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityFan)tileentity, pos);
		return ActionResultType.SUCCESS;
		}
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntityFan tile = (TileEntityFan) world.getTileEntity(pos);
		if (tile != null) {
			InventoryHelper.dropInventoryItems(world, pos, tile);
			world.updateComparatorOutputLevel(pos, this);
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean flag = world.isBlockPowered(pos);
		if (flag != state.get(POWERED))
			world.setBlockState(pos, state.with(POWERED, flag), 4);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!world.isRemote) {
			boolean flag = !world.isBlockPowered(pos);
			if (flag != state.get(POWERED))
				world.setBlockState(pos, state.with(POWERED, flag), 4);
		}
	}
}
