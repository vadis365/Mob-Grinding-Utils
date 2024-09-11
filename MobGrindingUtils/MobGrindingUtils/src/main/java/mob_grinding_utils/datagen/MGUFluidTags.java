package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import mob_grinding_utils.util.RL;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MGUFluidTags extends FluidTagsProvider {
    public MGUFluidTags(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> something, ExistingFileHelper existingFileHelper) {
        super(generatorIn.getPackOutput(), something, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        tag(ModTags.Fluids.EXPERIENCE).add(ModBlocks.FLUID_XP.get());
        tag(ModTags.Fluids.XPJUICE).add(ModBlocks.FLUID_XP.get());

        tag(ModTags.Fluids.EXPERIENCE).addOptional(RL.rl("pneumaticcraft", "memory_essence"));
        tag(ModTags.Fluids.EXPERIENCE).addOptional(RL.rl("cofh_core","experience"));
    }
}
