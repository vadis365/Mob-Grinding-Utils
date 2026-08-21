package mob_grinding_utils;

import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

public final class ModColourManager {
    private ModColourManager() {}

    public static void registerBlockTintSources(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(BlockTintSources.constant(0xFF993299)), ModBlocks.DREADFUL_DIRT.getBlock());
        event.register(List.of(BlockTintSources.constant(0xFFBEFF00)), ModBlocks.DELIGHTFUL_DIRT.getBlock());
    }
}
