package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(true, new Recipes(generator));

        MGUBlockTags blockTags = new MGUBlockTags(generator, event.getExistingFileHelper());
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new MGULootTables(generator));
        generator.addProvider(true, new MGUFluidTags(generator, event.getExistingFileHelper()));
        generator.addProvider(true, new MGUEntityTypeTags(generator, event.getExistingFileHelper()));
        generator.addProvider(true, new BlockStates(generator, event.getExistingFileHelper()));
    }
}
