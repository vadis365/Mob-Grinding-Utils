package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator gen = event.getGenerator();
        event.createDatapackRegistryObjects(MGUDamageType.registries());
        gen.addProvider(true, new Recipes(gen, event.getLookupProvider()));
        gen.addProvider(true, new MGUBlockTags(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, MGULootTables.getProvider(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUFluidTags(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUEntityTypeTags(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUBiomeTags(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(true, new MGUDamageTags(gen.getPackOutput(), event.getLookupProvider()));
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(true, new MGUBlockStates(gen.getPackOutput()));
    }
}
