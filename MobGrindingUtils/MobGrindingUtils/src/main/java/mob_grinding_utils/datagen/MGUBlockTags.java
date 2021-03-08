package mob_grinding_utils.datagen;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MGUBlockTags extends BlockTagsProvider {
    public MGUBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        Builder<Block> witherTag = this.getOrCreateBuilder(BlockTags.WITHER_IMMUNE);
        Builder<Block> dragonTag = this.getOrCreateBuilder(BlockTags.DRAGON_IMMUNE);

        for (Block block : ModBlocks.BLOCKS) {
            witherTag.add(block);
            dragonTag.add(block);
        }
    }
}
