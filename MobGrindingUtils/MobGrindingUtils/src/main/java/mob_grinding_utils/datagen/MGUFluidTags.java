package mob_grinding_utils.datagen;

import mob_grinding_utils.MobGrindingUtils;
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
        getOrCreateBuilder(MobGrindingUtils.EXPERIENCE).add(ModBlocks.FLUID_XP);
        getOrCreateBuilder(MobGrindingUtils.XPJUICE).add(ModBlocks.FLUID_XP);
    }
}
