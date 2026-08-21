package mob_grinding_utils.datagen;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import mob_grinding_utils.util.RL;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUBiomeTags extends BiomeTagsProvider {
    public MGUBiomeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Reference.MOD_ID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider pointless) {
        tag(ModTags.Biomes.PASSIVE_OVERRIDE).addTags(Tags.Biomes.IS_CAVE);
        tag(ModTags.Biomes.PASSIVE_OVERRIDE).addTags(BiomeTags.IS_RIVER);

        getOrCreateRawBuilder(ModTags.Biomes.HOSTILE_OVERRIDE);

		tag(ModTags.Biomes.HOSTILE_OVERRIDE).addOptional(ResourceKey.create(Registries.BIOME, RL.rl("javd", "void")));
		tag(ModTags.Biomes.HOSTILE_OVERRIDE).addOptional(ResourceKey.create(Registries.BIOME, RL.rl("jamd", "mining")));
    }
}
