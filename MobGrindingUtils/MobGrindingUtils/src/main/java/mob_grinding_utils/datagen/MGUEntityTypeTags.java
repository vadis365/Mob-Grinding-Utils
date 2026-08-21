package mob_grinding_utils.datagen;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUEntityTypeTags extends EntityTypeTagsProvider {
    public MGUEntityTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Reference.MOD_ID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        this.tag(ModTags.Entities.NO_SWAB).add(EntityType.WITHER);
        this.tag(ModTags.Entities.NO_SWAB).add(EntityType.ENDER_DRAGON);

        getOrCreateRawBuilder(ModTags.Entities.NO_SPAWN);

        getOrCreateRawBuilder(ModTags.Entities.NO_DREADFUL_SPAWN);
        getOrCreateRawBuilder(ModTags.Entities.NO_DELIGHTFUL_SPAWN);
		getOrCreateRawBuilder(ModTags.Entities.NO_DIRT_SPAWN).addOptionalElement(Identifier.fromNamespaceAndPath("alexsmobs", "farseer"));
		getOrCreateRawBuilder(ModTags.Entities.NO_DIRT_SPAWN).addOptionalElement(Identifier.fromNamespaceAndPath("alexsmobs", "centipede_head"));
    }
}
