package mob_grinding_utils;

import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

public class ModColourManager {
	/** Purple top/overlay tint for Dreadful Dirt (RGB 153, 50, 153). Keep in sync with items/dreadful_dirt.json. */
	public static final int DREADFUL_DIRT_COLOUR = ARGB.opaque(0x993299);
	/** Lime top/overlay tint for Delightful Dirt (RGB 190, 255, 0). Keep in sync with items/delightful_dirt.json. */
	public static final int DELIGHTFUL_DIRT_COLOUR = ARGB.opaque(0xBEFF00);

	public static void registerBlockHandlers(RegisterColorHandlersEvent.BlockTintSources event) {
		// Must be opaque ARGB: cutout overlay quads multiply vertex alpha; RGB-only (alpha 0) hides the fringe in-world
		// while the solid top still shows color. Item tints call ARGB.opaque automatically via Constant.
		event.register(List.of(BlockTintSources.constant(DREADFUL_DIRT_COLOUR)), ModBlocks.DREADFUL_DIRT.getBlock());
		event.register(List.of(BlockTintSources.constant(DELIGHTFUL_DIRT_COLOUR)), ModBlocks.DELIGHTFUL_DIRT.getBlock());
	}
}
