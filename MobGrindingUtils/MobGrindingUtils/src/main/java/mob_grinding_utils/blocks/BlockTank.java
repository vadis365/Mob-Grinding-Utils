package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.BlockEntities.BlockEntityTank;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockTank extends BaseEntityBlock {
	public static final MapCodec<BlockTank> CODEC = simpleCodec(BlockTank::new);

    public BlockTank(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(1.0F, 2000.0F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

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
		return new BlockEntityTank(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide() ? null : BlockEntityTank::serverTick;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		// BER draws the glass tank + fluid; block model must not cover it with an opaque cube.
		return RenderShape.INVISIBLE;
	}

	@Nonnull
	@Override
	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide())
			return InteractionResult.SUCCESS;
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof BlockEntityTank) {
			Optional<ResourceHandler<FluidResource>> fluidHandler = CapHelper.getFluidHandler(world, pos, hit.getDirection());
			fluidHandler.ifPresent((handler) -> {
				if (player.getItemInHand(hand).isEmpty() || !FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getDirection())) {
					FluidStack fluid = FluidUtil.getStack(handler, 0);
					int capacity = handler.getCapacityAsInt(0, handler.getResource(0));
					if (!fluid.isEmpty())
						player.sendOverlayMessage(Component.literal(fluid.getHoverName().getString() + ": " + fluid.getAmount() + "/" + capacity));
					else
						player.sendOverlayMessage(Component.literal("Empty: 0/" + capacity));
				}
			});
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}
}
