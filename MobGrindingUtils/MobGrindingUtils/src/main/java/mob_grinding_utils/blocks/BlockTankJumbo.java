package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityJumboTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;

public class BlockTankJumbo extends BlockTank {
    public BlockTankJumbo(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(1.0F, 2000.0F)
                .sound(SoundType.METAL)
                .noOcclusion()
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

    public BlockTankJumbo(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntityJumboTank(pos, state);
	}
}
