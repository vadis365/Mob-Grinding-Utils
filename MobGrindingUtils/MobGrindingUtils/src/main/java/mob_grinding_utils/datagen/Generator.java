package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(true, new Recipes(gen, event.getLookupProvider()));

        MGUBlockTags blockTags = new MGUBlockTags(gen, event.getLookupProvider(), event.getExistingFileHelper());
        gen.addProvider(true, blockTags);
        gen.addProvider(true, MGULootTables.getProvider(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUFluidTags(gen, event.getLookupProvider(), event.getExistingFileHelper()));
        gen.addProvider(true, new MGUEntityTypeTags(gen, event.getLookupProvider(), event.getExistingFileHelper()));
        gen.addProvider(true, new MGUBlockStates(gen, event.getExistingFileHelper()));
        gen.addProvider(true, new MGUBiomeTags(gen, event.getLookupProvider(), event.getExistingFileHelper()));
        gen.addProvider(true, new MGUDamageType(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUDamageTags(gen.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
    }
}
