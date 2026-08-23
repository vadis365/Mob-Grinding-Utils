package mob_grinding_utils.datagen;

import mob_grinding_utils.Reference;
import mob_grinding_utils.util.RL;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUDamageTags extends DamageTypeTagsProvider {
    public MGUDamageTags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Reference.MOD_ID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_ARMOR).addOptionalElement(RL.rl(Reference.MOD_ID, "spikes"));
    }
}
