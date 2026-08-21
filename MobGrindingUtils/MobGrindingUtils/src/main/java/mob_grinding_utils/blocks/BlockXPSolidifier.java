package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.tile.TileEntityXPSolidifier;
import mob_grinding_utils.util.CapHelper;
import mob_grinding_utils.util.FluidTransfer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class BlockXPSolidifier extends BaseEntityBlock {
	public static final MapCodec<BlockXPSolidifier> CODEC = simpleCodec(BlockXPSolidifier::new);
	public static final EnumProperty FACING = HorizontalDirectionalBlock.FACING;
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
	protected void affectNeighborsAfterRemoval(@Nonnull BlockState state, @Nonnull net.minecraft.server.level.ServerLevel level, @Nonnull BlockPos pos, boolean movedByPiston) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof TileEntityXPSolidifier entity) {
				for (int slot = 0; slot < 2; slot++)
					if (!entity.getInputStack(slot).isEmpty())
						Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.getInputStack(slot));
				if (!entity.getOutputStack().isEmpty())
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.getOutputStack());
		}
		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

    @Nonnull
	@Override
    public InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof TileEntityXPSolidifier entityXPSolidifier) {
				if (!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BucketItem) { // fixy later, Flanks: ?!?
					Optional<ResourceHandler<FluidResource>> fluidHandler = CapHelper.getFluidHandler(level, pos, hit.getDirection());
					fluidHandler.ifPresent((handler) -> {
						FluidResource fluid = handler.getResource(0);
						if (player.getItemInHand(hand).isEmpty() && !fluid.isEmpty())
							if (player instanceof ServerPlayer serverPlayer)
								serverPlayer.sendSystemMessage(Component.literal(fluid.getHoverName().getString() + ": "+ handler.getAmountAsInt(0)+"/"+handler.getCapacityAsInt(0, fluid)), true);
						else
							FluidTransfer.interact(player, hand, handler);
					});
					return InteractionResult.SUCCESS;
				}
				else {
					player.openMenu(entityXPSolidifier, pos);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
    }

}
