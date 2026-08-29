package mob_grinding_utils.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class BlockDarkOakStone extends Block {

    public BlockDarkOakStone(Identifier id) {
        this(Block.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .strength(1.5F, 10F)
                .sound(SoundType.STONE)
                .lightLevel(bState -> 7)
                .setId(ResourceKey.create(Registries.BLOCK, id)));
    }

    public BlockDarkOakStone(Block.Properties properties) {
		super(properties);
	}
}
