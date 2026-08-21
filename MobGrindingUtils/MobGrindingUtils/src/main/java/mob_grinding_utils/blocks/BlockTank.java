package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.util.CapHelper;
import mob_grinding_utils.util.FluidTransfer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
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
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

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
		return pLevel.isClientSide() ? null : TileEntityTank::serverTick;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Nonnull
	@Override
	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide())
			return InteractionResult.SUCCESS;
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof TileEntityTank) {
			Optional<ResourceHandler<FluidResource>> fluidHandler = CapHelper.getFluidHandler(world, pos, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (player.getItemInHand(hand).isEmpty() || !FluidTransfer.interact(player, hand, handler)) {
					FluidResource fluid = handler.getResource(0);
					if (player instanceof ServerPlayer serverPlayer)
						serverPlayer.sendSystemMessage(Component.literal(!fluid.isEmpty() ? fluid.getHoverName().getString() + ": " + handler.getAmountAsInt(0) + "/" + handler.getCapacityAsInt(0, fluid) : "Empty: 0/" + handler.getCapacityAsInt(0, fluid)), true);
				}
			});
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
