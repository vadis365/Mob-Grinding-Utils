package mob_grinding_utils.datagen;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MGUEntityTypeTags extends EntityTypeTagsProvider {
    public MGUEntityTypeTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Entities.NO_SWAB).add(EntityType.WITHER);
        this.tag(ModTags.Entities.NO_SWAB).add(EntityType.ENDER_DRAGON);

        getOrCreateRawBuilder(ModTags.Entities.NO_SPAWN);

        getOrCreateRawBuilder(ModTags.Entities.NO_DREADFUL_SPAWN);
        getOrCreateRawBuilder(ModTags.Entities.NO_DELIGHTFUL_SPAWN);
        tag(ModTags.Entities.NO_DIRT_SPAWN).addOptional(new ResourceLocation("alexmobs", "farseer"));
    }
}
