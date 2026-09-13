package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.BlockEntities.BlockEntityXPSolidifier;
import mob_grinding_utils.util.CapHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class BlockXPSolidifier extends BaseEntityBlock {
    public static final MapCodec<BlockXPSolidifier> CODEC = simpleCodec(BlockXPSolidifier::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public BlockXPSolidifier(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(1.0F, 2000.0F)
                .sound(SoundType.METAL)
                .noOcclusion()
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

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
        return new BlockEntityXPSolidifier(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
        return BlockEntityXPSolidifier::tick;
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
    protected void affectNeighborsAfterRemoval(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, boolean movedByPiston) {
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
            if (blockEntity instanceof BlockEntityXPSolidifier entityXPSolidifier) {
                if (!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BucketItem) { // fixy later, Flanks: ?!?
                    Optional<ResourceHandler<FluidResource>> fluidHandler = CapHelper.getFluidHandler(level, pos, hit.getDirection());
                    fluidHandler.ifPresent((handler) -> {
                        FluidStack fluid = FluidUtil.getStack(handler, 0);
                        int capacity = handler.getCapacityAsInt(0, handler.getResource(0));
                        if (player.getItemInHand(hand).isEmpty() && !fluid.isEmpty())
                            player.sendOverlayMessage(Component.translatable(fluid.getHoverName().getString() + ": "+ fluid.getAmount()+"/"+capacity));
                        else
                            FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection());
                    });
                    return InteractionResult.SUCCESS;
                }
                else {
                    player.openMenu(entityXPSolidifier, pos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    public void appendHoverText(ItemStack stack, @Nonnull Item.TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag tooltipFlag) {
        if (stack.has(MGUComponents.FLUID)) {
            FluidStack fluid = stack.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get();
            if (!fluid.isEmpty()) {
                builder.accept(Component.literal("Contains: " + fluid.getHoverName().getString()).withStyle(ChatFormatting.GREEN));
                builder.accept(Component.literal(String.format("%dMb/%dMb", fluid.getAmount(), 16000)).withStyle(ChatFormatting.BLUE));
            }
        }
    }
}
