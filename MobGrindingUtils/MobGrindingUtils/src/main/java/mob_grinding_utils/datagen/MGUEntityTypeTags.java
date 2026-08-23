package mob_grinding_utils.datagen;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import mob_grinding_utils.util.RL;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUEntityTypeTags extends EntityTypeTagsProvider {
    public MGUEntityTypeTags(DataGenerator generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator.getPackOutput(), registries, Reference.MOD_ID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        this.tag(ModTags.Entities.NO_SWAB).add(EntityType.WITHER);
        this.tag(ModTags.Entities.NO_SWAB).add(EntityType.ENDER_DRAGON);

        getOrCreateRawBuilder(ModTags.Entities.NO_SPAWN);

        getOrCreateRawBuilder(ModTags.Entities.NO_DREADFUL_SPAWN);
        getOrCreateRawBuilder(ModTags.Entities.NO_DELIGHTFUL_SPAWN);
        getOrCreateRawBuilder(ModTags.Entities.NO_DIRT_SPAWN)
            .addOptionalElement(RL.rl("alexsmobs", "farseer"))
            .addOptionalElement(RL.rl("alexsmobs", "centipede_head"));
    }
}
