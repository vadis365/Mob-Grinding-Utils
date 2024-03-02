package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class  MGUBlockTags extends BlockTagsProvider {
    public MGUBlockTags(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> something, ExistingFileHelper existingFileHelper) {
        super(generatorIn.getPackOutput(), something, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider pointless) {
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
