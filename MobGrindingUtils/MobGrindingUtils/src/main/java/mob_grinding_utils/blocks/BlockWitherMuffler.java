package mob_grinding_utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;

public class BlockWitherMuffler extends Block {

    public static final BooleanProperty MODE = BooleanProperty.create("mode");

    public BlockWitherMuffler(Identifier id) {
        this(Block.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.5F, 2000F)
            .sound(SoundType.WOOL)
            .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

    public BlockWitherMuffler(Block.Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(MODE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MODE);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(MODE, false);
    }

    @Nonnull
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hit) {
        boolean swap = !state.getValue(MODE);
        if (!world.isClientSide())
            world.setBlock(pos, state.setValue(MODE, swap), 3);
        CompoundTag nbt = player.getPersistentData();
        nbt.putBoolean("MGU_WitherMuffle", swap);
        player.sendOverlayMessage(Component.literal(swap ? "Now hiding Wither boss bars.":"Now showing Wither boss bars.")); //TODO check this
        return InteractionResult.SUCCESS;
    }
}