package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import mob_grinding_utils.util.RL;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUFluidTags extends FluidTagsProvider {
    public MGUFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Reference.MOD_ID);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        tag(ModTags.Fluids.EXPERIENCE).add(ModBlocks.FLUID_XP.get());
        tag(ModTags.Fluids.XPJUICE).add(ModBlocks.FLUID_XP.get());

		getOrCreateRawBuilder(ModTags.Fluids.EXPERIENCE).addOptionalElement(RL.rl("pneumaticcraft", "memory_essence"));
		getOrCreateRawBuilder(ModTags.Fluids.EXPERIENCE).addOptionalElement(RL.rl("cofh_core","experience"));
    }
}
