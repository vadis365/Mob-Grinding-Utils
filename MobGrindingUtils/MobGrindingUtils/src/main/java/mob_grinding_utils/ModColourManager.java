package mob_grinding_utils;

import java.awt.Color;

import mob_grinding_utils.blocks.BlockDelightfulDirt;
import mob_grinding_utils.blocks.BlockDreadfulDirt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;

public class ModColourManager {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	public static void registerColourHandlers() {
		final BlockColors blockColors = MINECRAFT.getBlockColors();
		final ItemColors itemColors = MINECRAFT.getItemColors();

		registerBlockColourHandlers(blockColors);
		registerItemColourHandlers(blockColors, itemColors);
	}

	private static void registerBlockColourHandlers(final BlockColors blockColors) {
		final BlockColor dreadfulDirtColour = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null)
				return new Color(153, 50, 153).getRGB() & 0x00ffffff;
			return new Color(153, 50, 153).getRGB() & 0x00ffffff;
		};

		final BlockColor delightfulDirtColour = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null)
				return new Color(190, 255, 0).getRGB() & 0x00ffffff;
			return new Color(190, 255, 0).getRGB() & 0x00ffffff;
		};

		blockColors.register(dreadfulDirtColour, ModBlocks.DREADFUL_DIRT.getBlock());
		blockColors.register(delightfulDirtColour, ModBlocks.DELIGHTFUL_DIRT.getBlock());
	}

	private static void registerItemColourHandlers(final BlockColors blockColors, final ItemColors itemColors) {
		final ItemColor itemBlockColourHandler = (stack, tintIndex) -> {
			@SuppressWarnings("deprecation")
			final BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
			if(state.getBlock() instanceof BlockDreadfulDirt)
				return blockColors.getColor(state, null, null, tintIndex);
			if(state.getBlock() instanceof BlockDelightfulDirt)
				return blockColors.getColor(state, null, null, tintIndex);
			return -1;
		};

		itemColors.register(itemBlockColourHandler, ModBlocks.DREADFUL_DIRT.getItem());
		itemColors.register(itemBlockColourHandler, ModBlocks.DELIGHTFUL_DIRT.getItem());
	}
}

