package mob_grinding_utils.datagen;

import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MGULootTables extends BlockLootSubProvider {
    protected MGULootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    public static LootTableProvider getProvider(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(MGULootTables::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.ABSORPTION_HOPPER.getBlock());
        dropSelf(ModBlocks.DARK_OAK_STONE.getBlock());
        dropSelf(ModBlocks.DRAGON_MUFFLER.getBlock());
        dropSelf(ModBlocks.WITHER_MUFFLER.getBlock());
        dropSelf(ModBlocks.ENTITY_CONVEYOR.getBlock());
        dropSelf(ModBlocks.FAN.getBlock());
        dropSelf(ModBlocks.SAW.getBlock());
        dropSelf(ModBlocks.SPIKES.getBlock());
        dropSelf(ModBlocks.XP_TAP.getBlock());
        dropSelf(ModBlocks.ENDER_INHIBITOR_ON.getBlock());
        dropOther(ModBlocks.ENDER_INHIBITOR_OFF.getBlock(), ModBlocks.ENDER_INHIBITOR_ON.getItem());
        dropSelf(ModBlocks.TINTED_GLASS.getBlock());
        add(ModBlocks.DREADFUL_DIRT.getBlock(), (block) -> createSingleItemTableWithSilkTouch(block, Items.DIRT));
        add(ModBlocks.DELIGHTFUL_DIRT.getBlock(), (block) -> createSingleItemTableWithSilkTouch(block, Items.DIRT));
        dropSelf(ModBlocks.SOLID_XP_BLOCK.getBlock());
        dropSelf(ModBlocks.ENTITY_SPAWNER.getBlock());
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<Block> ignoreList = ImmutableSet.of(
            ModBlocks.TANK.getBlock(),
            ModBlocks.TANK_SINK.getBlock(),
            ModBlocks.JUMBO_TANK.getBlock(),
            ModBlocks.XPSOLIDIFIER.getBlock(),
            ModBlocks.FLUID_XP_BLOCK.get()
        );

        return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get)
            .filter(e -> !ignoreList.contains(e)).collect(Collectors.toList());
    }
}
