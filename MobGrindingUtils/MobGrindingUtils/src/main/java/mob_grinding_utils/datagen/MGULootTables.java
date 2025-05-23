package mob_grinding_utils.datagen;

import com.google.common.collect.ImmutableSet;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MGULootTables extends BlockLootSubProvider {
    protected MGULootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    public static LootTableProvider getProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        return new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(MGULootTables::new, LootContextParamSets.BLOCK)), future);
    }

    @Override
    protected void generate() {
        CopyComponentsFunction.Builder copyFluid = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                .include(MGUComponents.FLUID.get());

        dropComponents(ModBlocks.TANK, $ -> $.apply(copyFluid));
        dropComponents(ModBlocks.TANK_SINK, $ -> $.apply(copyFluid));
        dropComponents(ModBlocks.JUMBO_TANK, $ -> $.apply(copyFluid));
        dropComponents(ModBlocks.XPSOLIDIFIER, $ -> $.apply(copyFluid));

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

    private void dropComponents(Supplier<? extends Block> blockSupplier, Consumer<LootPool.Builder> lootFunctionSupplier) {
        LootPool.Builder lootPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(blockSupplier.get()));
        lootFunctionSupplier.accept(lootPool);
        add(blockSupplier.get(), LootTable.lootTable().withPool(applyExplosionCondition(blockSupplier.get(), lootPool)));
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<Block> ignoreList = ImmutableSet.of(
            ModBlocks.FLUID_XP_BLOCK.get()
        );

        return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get)
            .filter(e -> !ignoreList.contains(e)).collect(Collectors.toList());
    }
}
