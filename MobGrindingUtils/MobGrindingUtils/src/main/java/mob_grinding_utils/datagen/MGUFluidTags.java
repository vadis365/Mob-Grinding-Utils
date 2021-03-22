package mob_grinding_utils.datagen;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MGUFluidTags extends FluidTagsProvider {
    public MGUFluidTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        ITag.INamedTag<Fluid> experience = FluidTags.makeWrapperTag(new ResourceLocation("forge", "experience").toString());

        getOrCreateBuilder(experience).add(ModBlocks.FLUID_XP);
    }
}
