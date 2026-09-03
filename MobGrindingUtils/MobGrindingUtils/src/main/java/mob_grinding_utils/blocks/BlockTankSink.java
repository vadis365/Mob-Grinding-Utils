package mob_grinding_utils.blocks;

import mob_grinding_utils.BlockEntities.BlockEntitySinkTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTankSink extends BlockTank {

    public BlockTankSink(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(1.0F, 2000.0F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }
	public BlockTankSink(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new BlockEntitySinkTank(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide() ? null : BlockEntitySinkTank::serverTick;
	}
}
