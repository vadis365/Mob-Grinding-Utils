package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(true, new Recipes(generator));

        MGUBlockTags blockTags = new MGUBlockTags(generator, event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(true, blockTags);
        generator.addProvider(true, MGULootTables.getProvider(generator.getPackOutput()));
        generator.addProvider(true, new MGUFluidTags(generator, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(true, new MGUEntityTypeTags(generator, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(true, new BlockStates(generator, event.getExistingFileHelper()));
        generator.addProvider(true, new MGUBiomeTags(generator, event.getLookupProvider(), event.getExistingFileHelper()));
    }
}
