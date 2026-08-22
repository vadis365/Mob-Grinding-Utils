package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(true, new Recipes(event., gen.getPackOutput()));

        MGUBlockTags blockTags = new MGUBlockTags(gen, event.getLookupProvider());
        gen.addProvider(true, blockTags);
        gen.addProvider(true, MGULootTables.getProvider(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUFluidTags(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUEntityTypeTags(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUBlockStates(gen));
        gen.addProvider(true, new MGUBiomeTags(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUDamageType(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUDamageTags(gen.getPackOutput(), event.getLookupProvider()));
    }
}
