package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator gen = event.getGenerator();
        event.createDatapackRegistryObjects(MGUDamageType.registries());
        gen.addProvider(true, new MGURecipes(gen, event.getLookupProvider()));
        MGUBlockTags blockTags = new MGUBlockTags(gen, event.getLookupProvider());
        gen.addProvider(true, blockTags);
        gen.addProvider(true, MGULootTables.getProvider(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUFluidTags(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUEntityTypeTags(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUBiomeTags(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUDamageTags(gen.getPackOutput(), event.getLookupProvider()));
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(true, new MGUBlockStates(gen));
    }
}
