package mob_grinding_utils.datagen;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MGUFluidTags extends FluidTagsProvider {
    public MGUFluidTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(MobGrindingUtils.EXPERIENCE).add(ModBlocks.FLUID_XP.get());
        tag(MobGrindingUtils.XPJUICE).add(ModBlocks.FLUID_XP.get());

        tag(MobGrindingUtils.EXPERIENCE).addOptional(new ResourceLocation("pneumaticcraft","memory_essence"));
        tag(MobGrindingUtils.EXPERIENCE).addOptional(new ResourceLocation("cofh_core","experience"));
    }
}
