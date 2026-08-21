package mob_grinding_utils.datagen;

import mob_grinding_utils.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.tags.DamageTypeTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUDamageTags extends DamageTypeTagsProvider {
    public MGUDamageTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Reference.MOD_ID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
		tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "spikes")));
    }
}
