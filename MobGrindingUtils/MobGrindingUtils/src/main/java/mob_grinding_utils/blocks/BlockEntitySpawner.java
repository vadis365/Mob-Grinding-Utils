package mob_grinding_utils.blocks;

import java.util.Random;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

@SuppressWarnings("deprecation")
public class BlockEntitySpawner extends Block implements ITileEntityProvider {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public BlockEntitySpawner(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityMGUSpawner();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
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
			if (tileentity  instanceof TileEntityMGUSpawner)
			NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityMGUSpawner)tileentity, pos);
		return ActionResultType.SUCCESS;
		} 
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote && !player.abilities.isCreativeMode) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getTileEntity(pos);
			if (tile != null) {
				if(!tile.inputSlots.getStackInSlot(0).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(0));
				if(!tile.inputSlots.getStackInSlot(1).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(1));
				if(!tile.inputSlots.getStackInSlot(2).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(2));
				if(!tile.inputSlots.getStackInSlot(3).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(3));
				if(!tile.fuelSlot.getStackInSlot(0).isEmpty())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.fuelSlot.getStackInSlot(0));
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.isIn(newState.getBlock())) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getTileEntity(pos);
			if (tile != null) {
				//InventoryHelper.dropInventoryItems(world, pos, tile);
				world.updateComparatorOutputLevel(pos, this);
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!world.isRemote) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getTileEntity(pos);
			boolean flag = state.get(POWERED);
			if (flag != world.isBlockPowered(pos))
				if (flag)
					world.getPendingBlockTicks().scheduleTick(pos, this, 4);
				else {
					world.setBlockState(pos, state.func_235896_a_(POWERED), 2);
					if (tile != null)
						tile.isOn = !state.get(POWERED);
				}
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!world.isRemote) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getTileEntity(pos);
			if (state.get(POWERED) && !world.isBlockPowered(pos)) {
				world.setBlockState(pos, state.func_235896_a_(POWERED), 2);
				if (tile != null)
					tile.isOn = !state.get(POWERED);
			}
		}
	}

	@Override
	public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos) {
		return state.isIn(ModBlocks.ENTITY_SPAWNER);
	}
}
