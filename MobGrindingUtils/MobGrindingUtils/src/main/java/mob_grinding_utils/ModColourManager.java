package mob_grinding_utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.awt.*;
import java.util.List;

public class ModColourManager {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    private static final BlockTintSource DREADFUL_DIRT_COLOUR = BlockTintSources.constant(new Color(153, 50, 153).getRGB(), new Color(153, 50, 153).getRGB());
    private static final BlockTintSource DELIGHTFUL_DIRT_COLOUR = BlockTintSources.constant(new Color(190, 255, 0).getRGB(), new Color(190, 255, 0).getRGB());

    public static void registerBlockHandlers(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(DREADFUL_DIRT_COLOUR), ModBlocks.DREADFUL_DIRT.getBlock());
        event.register(List.of(DELIGHTFUL_DIRT_COLOUR), ModBlocks.DELIGHTFUL_DIRT.getBlock());
    }
/*	public static void registerItemHandlers(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(List.of(DREADFUL_DIRT_COLOUR), ModBlocks.DREADFUL_DIRT.getItem());
        event.register(List.of(DELIGHTFUL_DIRT_COLOUR), ModBlocks.DELIGHTFUL_DIRT.getItem());
    }*/
}

