package mob_grinding_utils.blocks;

import javax.annotation.Nullable;

import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockXPSolidifier extends ContainerBlock {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public BlockXPSolidifier(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityXPSolidifier();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, direction);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityXPSolidifier)
                NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityXPSolidifier) tileentity, pos);
            return ActionResultType.SUCCESS;
        }
    }
}
