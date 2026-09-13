package mob_grinding_utils.blocks;

import mob_grinding_utils.BlockEntities.BlockEntityMGUSpawner;
import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockEntitySpawner extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockEntitySpawner(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(10.0F, 2000.0F)
                .sound(SoundType.METAL)
                .noOcclusion()
                .randomTicks()
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

    public BlockEntitySpawner(Block.Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new BlockEntityMGUSpawner(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? BlockEntityMGUSpawner::clientTick : BlockEntityMGUSpawner::serverTick;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Nonnull
    @Override
    public InteractionResult useWithoutItem(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
        if (!world.isClientSide()) {
            BlockEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof BlockEntityMGUSpawner tile)
                player.openMenu(tile, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState playerWillDestroy(Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        if (!world.isClientSide() && !player.getAbilities().instabuild) {
            BlockEntityMGUSpawner tile = (BlockEntityMGUSpawner) world.getBlockEntity(pos);
            if (tile != null) {
                for (int i = 0; i < tile.inputSlots.size(); i++) {
                    ItemStack stack = net.neoforged.neoforge.transfer.item.ItemUtil.getStack(tile.inputSlots, i);
                    if (!stack.isEmpty())
                        Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
                ItemStack fuel = net.neoforged.neoforge.transfer.item.ItemUtil.getStack(tile.fuelSlot, 0);
                if (!fuel.isEmpty())
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), fuel);
                world.removeBlockEntity(pos);
            }
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, boolean isMoving) {
        world.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    protected void neighborChanged(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nullable Orientation orientation, boolean isMoving) {
        if (!world.isClientSide()) {
            BlockEntityMGUSpawner tile = (BlockEntityMGUSpawner) world.getBlockEntity(pos);
            boolean flag = state.getValue(POWERED);
            if (flag != world.hasNeighborSignal(pos))
                if (flag)
                    world.scheduleTick(pos, this, 4);
                else {
                    world.setBlock(pos, state.cycle(POWERED), 2);
                    if (tile != null)
                        tile.isOn = !state.getValue(POWERED);
                }
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (!world.isClientSide()) {
            BlockEntityMGUSpawner tile = (BlockEntityMGUSpawner) world.getBlockEntity(pos);
            if (state.getValue(POWERED) && !world.hasNeighborSignal(pos)) {
                world.setBlock(pos, state.cycle(POWERED), 2);
                if (tile != null)
                    tile.isOn = !state.getValue(POWERED);
            }
        }
    }

    @Override
    public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos) {
        return state.is(ModBlocks.ENTITY_SPAWNER.getBlock());
    }
}
