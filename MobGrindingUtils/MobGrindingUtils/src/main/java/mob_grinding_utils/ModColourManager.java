package mob_grinding_utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.awt.*;

public class ModColourManager {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	private static final BlockColor DREADFUL_DIRT_COLOUR = (state, blockAccess, pos, tintIndex) -> new Color(153, 50, 153).getRGB() & 0x00ffffff;
	private static final BlockColor DELIGHTFUL_DIRT_COLOUR = (state, blockAccess, pos, tintIndex) -> new Color(190, 255, 0).getRGB() & 0x00ffffff;
	private static final ItemColor DREADFUL_DIRT_ITEM_COLOUR = (stack, tintIndex) -> new Color(153, 50, 153).getRGB() & 0x00ffffff;
	private static final ItemColor DELIGHTFUL_DIRT_ITEM_COLOUR = (stack, tintIndex) -> new Color(190, 255, 0).getRGB() & 0x00ffffff;


	public static void registerBlockHandlers(RegisterColorHandlersEvent.Block event) {
		event.register(DREADFUL_DIRT_COLOUR, ModBlocks.DREADFUL_DIRT.getBlock());
		event.register(DELIGHTFUL_DIRT_COLOUR, ModBlocks.DELIGHTFUL_DIRT.getBlock());
	}
	public static void registerItemHandlers(RegisterColorHandlersEvent.Item event) {
		event.register(DREADFUL_DIRT_ITEM_COLOUR, ModBlocks.DREADFUL_DIRT.getItem());
		event.register(DELIGHTFUL_DIRT_ITEM_COLOUR, ModBlocks.DELIGHTFUL_DIRT.getItem());
	}
}

