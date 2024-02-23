package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class  MGUBlockTags extends BlockTagsProvider {
    public MGUBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        var witherTag = this.tag(BlockTags.WITHER_IMMUNE);
        var dragonTag = this.tag(BlockTags.DRAGON_IMMUNE);
        var pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);

        ModBlocks.BLOCKS.getEntries().forEach((block) -> {
            witherTag.add(block.get());
            dragonTag.add(block.get());
            pickaxeTag.add(block.get());
        });
    }
}
