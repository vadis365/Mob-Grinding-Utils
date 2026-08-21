package mob_grinding_utils.datagen;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class Generator {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event instanceof GatherDataEvent.Server server) {
            server.createDatapackRegistryObjects(MGUDamageType.registries());
            gen.addProvider(true, new Recipes(gen, server.getLookupProvider()));
            gen.addProvider(true, new MGUBlockTags(gen.getPackOutput(), server.getLookupProvider()));
            gen.addProvider(true, MGULootTables.getProvider(gen.getPackOutput(), server.getLookupProvider()));
            gen.addProvider(true, new MGUFluidTags(gen.getPackOutput(), server.getLookupProvider()));
            gen.addProvider(true, new MGUEntityTypeTags(gen.getPackOutput(), server.getLookupProvider()));
            gen.addProvider(true, new MGUBiomeTags(gen.getPackOutput(), server.getLookupProvider()));
            gen.addProvider(true, new MGUDamageTags(gen.getPackOutput(), server.getLookupProvider()));
        } else if (event instanceof GatherDataEvent.Client client) {
            gen.addProvider(true, new MGUBlockStates(gen.getPackOutput()));
        }
    }
}
