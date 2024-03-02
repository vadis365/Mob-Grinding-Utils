package mob_grinding_utils.datagen;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MGUDamageType extends DatapackBuiltinEntriesProvider {
    public MGUDamageType(PackOutput output, CompletableFuture<HolderLookup.Provider> lookUpThingIHate) {
        super(output, lookUpThingIHate, new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, $ -> $.register(MobGrindingUtils.SPIKE_TYPE, new DamageType("spikes", 1.0f))), Set.of(Reference.MOD_ID));
    }
}
