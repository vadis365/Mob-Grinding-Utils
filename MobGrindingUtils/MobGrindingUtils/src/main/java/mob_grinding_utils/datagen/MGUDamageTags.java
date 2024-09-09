package mob_grinding_utils.datagen;

import mob_grinding_utils.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MGUDamageTags extends DamageTypeTagsProvider {
    public MGUDamageTags(PackOutput output, CompletableFuture<HolderLookup.Provider> stupid, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, stupid, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "spikes"));
    }
}
