package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import net.minecraft.data.tags.TagsProvider.TagAppender;

public class  MGUBlockTags extends BlockTagsProvider {
    public MGUBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<Block> witherTag = this.tag(BlockTags.WITHER_IMMUNE);
        TagAppender<Block> dragonTag = this.tag(BlockTags.DRAGON_IMMUNE);
        TagAppender<Block> pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);

        ModBlocks.BLOCKS.getEntries().forEach((block) -> {
            witherTag.add(block.get());
            dragonTag.add(block.get());
            pickaxeTag.add(block.get());
        });
    }
}
