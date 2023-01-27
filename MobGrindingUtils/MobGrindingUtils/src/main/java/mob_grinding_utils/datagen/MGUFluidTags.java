package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModTags;
import mob_grinding_utils.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MGUFluidTags extends FluidTagsProvider {
    public MGUFluidTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(ModTags.Fluids.EXPERIENCE).add(ModBlocks.FLUID_XP.get());
        getOrCreateBuilder(ModTags.Fluids.XPJUICE).add(ModBlocks.FLUID_XP.get());

        getOrCreateBuilder(ModTags.Fluids.EXPERIENCE).addOptional(new ResourceLocation("pneumaticcraft","memory_essence"));
        getOrCreateBuilder(ModTags.Fluids.EXPERIENCE).addOptional(new ResourceLocation("cofh_core","experience"));
    }
}
