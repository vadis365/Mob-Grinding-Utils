package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.BlockEntities.BlockEntityFan;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockFan extends DirectionalBlock implements EntityBlock {
    public static final MapCodec<BlockFan> CODEC = simpleCodec(BlockFan::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockFan(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(10.0F, 2000.0F)
                .sound(SoundType.METAL)
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

    public BlockFan(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Nonnull
    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new BlockEntityFan(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
        return BlockEntityFan::tick;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Nonnull
    @Override
    public InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
        if (!world.isClientSide()) {
            BlockEntity tileentity = world.getBlockEntity(pos);
            if (tileentity  instanceof BlockEntityFan)
                player.openMenu((BlockEntityFan)tileentity, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void affectNeighborsAfterRemoval(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, boolean movedByPiston) {
        BlockEntityFan tile = (BlockEntityFan) level.getBlockEntity(pos);
        if (tile != null) {
            Containers.dropContents(level, pos, tile);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Block block, Orientation orientation, boolean movedByPiston) {
        boolean flag = world.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED))
            world.setBlock(pos, state.setValue(POWERED, flag), 4);
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (!world.isClientSide()) {
            boolean flag = !world.hasNeighborSignal(pos);
            if (flag != state.getValue(POWERED))
                world.setBlock(pos, state.setValue(POWERED, flag), 4);
        }
    }
}
