package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class BlockXPSolidifier extends BaseEntityBlock {
	public static final MapCodec<BlockXPSolidifier> CODEC = simpleCodec(BlockXPSolidifier::new);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public BlockXPSolidifier(Properties properties) {
        super(properties);
    }

	@Nonnull
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new TileEntityXPSolidifier(pos, state);
    }

    @Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return TileEntityXPSolidifier::tick;
	}

    @Nonnull
	@Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}

	@Override
	protected void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean movedByPiston) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof TileEntityXPSolidifier entity) {
				if(!entity.inputSlots.getStackInSlot(0).isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.inputSlots.getStackInSlot(0));
				if(!entity.inputSlots.getStackInSlot(1).isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.inputSlots.getStackInSlot(1));
				if(!entity.outputSlot.getStackInSlot(0).isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.outputSlot.getStackInSlot(0));
			}
		}

		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    @Nonnull
	@Override
    public ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (level.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof TileEntityXPSolidifier entityXPSolidifier) {
				if (!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BucketItem) { // fixy later, Flanks: ?!?
					Optional<IFluidHandler> fluidHandler = CapHelper.getFluidHandler(level, pos, hit.getDirection());
					fluidHandler.ifPresent((handler) -> {
						if (player.getItemInHand(hand).isEmpty() && !handler.getFluidInTank(0).isEmpty())
							player.displayClientMessage(Component.translatable(handler.getFluidInTank(0).getHoverName().getString() + ": "+ handler.getFluidInTank(0).getAmount()+"/"+handler.getTankCapacity(0)), true);
						else
							FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection());
					});
					return ItemInteractionResult.SUCCESS;
				}
				else {
					player.openMenu(entityXPSolidifier, pos);
					return ItemInteractionResult.SUCCESS;
				}
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
