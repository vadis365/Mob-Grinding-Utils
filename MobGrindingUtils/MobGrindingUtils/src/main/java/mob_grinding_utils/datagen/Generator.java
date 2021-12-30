package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(new Recipes(generator));

        MGUBlockTags blockTags = new MGUBlockTags(generator, event.getExistingFileHelper());
        generator.addProvider(blockTags);
        generator.addProvider(new MGULootTables(generator));
        generator.addProvider(new MGUFluidTags(generator, event.getExistingFileHelper()));
        generator.addProvider(new MGUEntityTypeTags(generator, event.getExistingFileHelper()));
        generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
    }
}
