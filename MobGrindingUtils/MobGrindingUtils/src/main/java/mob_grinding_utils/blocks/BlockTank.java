package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockTank extends BaseEntityBlock {
	public static final MapCodec<BlockTank> CODEC = simpleCodec(BlockTank::new);
	public BlockTank(Block.Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntityTank(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide ? null : TileEntityTank::serverTick;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Nonnull
	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide)
			return ItemInteractionResult.SUCCESS;
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof TileEntityTank) {
			Optional<IFluidHandler> fluidHandler = CapHelper.getFluidHandler(world, pos, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (player.getItemInHand(hand).isEmpty() || !FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getDirection())) {
					if (!handler.getFluidInTank(0).isEmpty())
						player.displayClientMessage(Component.literal(handler.getFluidInTank(0).getHoverName().getString() + ": " + handler.getFluidInTank(0).getAmount() + "/" + handler.getTankCapacity(0)), true);
					else
						player.displayClientMessage(Component.literal("Empty: 0/" + handler.getTankCapacity(0)), true);
				}
			});
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
