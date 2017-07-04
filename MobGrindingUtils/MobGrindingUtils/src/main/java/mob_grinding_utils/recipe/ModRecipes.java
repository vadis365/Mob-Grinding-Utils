package mob_grinding_utils.recipe;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	
	public static void addRecipes() {

		//Absorption Hopper
		addShapedRecipe(getResource("mob_grinding_utils:absorption_hopper"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.ABSORPTION_HOPPER), new Object[] {" E ", " O ", "OHO", 'E', Items.ENDER_EYE, 'O', Blocks.OBSIDIAN, 'H', Blocks.HOPPER});

		//Absorption Hopper Upgrade
		addShapedRecipe(getResource("mob_grinding_utils:absorption_upgrade"), getResource("mob_grinding_utils"), new ItemStack(ModItems.ABSORPTION_UPGRADE), " E ", "ERE", "OEO", 'E', Items.ENDER_PEARL, 'O', Blocks.OBSIDIAN, 'R', Items.REDSTONE);

		//Spikes
		addShapedRecipe(getResource("mob_grinding_utils:spikes"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.SPIKES), "   ", " S ", "SIS", 'S', Items.IRON_SWORD, 'I', Blocks.IRON_BLOCK);

		//Tank
		addShapedRecipe(getResource("mob_grinding_utils:tank"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.TANK), "IGI", "GGG", "IGI", 'I', Items.IRON_INGOT, 'G', Blocks.GLASS);
		
		//Tank Sink
		addShapedRecipe(getResource("mob_grinding_utils:tank_sink"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.TANK_SINK), " I ", "EHE", " T ", 'I', Blocks.IRON_BARS, 'H', Blocks.HOPPER, 'T', ModBlocks.TANK, 'E', Items.ENDER_EYE);

		//XP TAP
		addShapedRecipe(getResource("mob_grinding_utils:xp_tap"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.XP_TAP), "O  ", "II ", "I  ", 'O', Blocks.OBSIDIAN, 'I', Items.IRON_INGOT);

		//Fan
		addShapedRecipe(getResource("mob_grinding_utils:fan"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.FAN), "SIS", "IRI", "SIS", 'S', Blocks.STONE_SLAB, 'I', Items.IRON_INGOT, 'R', Blocks.REDSTONE_BLOCK);

		//Fan Upgrades
		addShapedRecipe(getResource("mob_grinding_utils:fan_upgrade_1"), getResource("mob_grinding_utils"), new ItemStack(ModItems.FAN_UPGRADE, 1, 0), "I I", "FFF", "I I", 'I', Items.IRON_INGOT, 'F', Items.FEATHER);
		addShapedRecipe(getResource("mob_grinding_utils:fan_upgrade_2"), getResource("mob_grinding_utils"), new ItemStack(ModItems.FAN_UPGRADE, 1, 1), "IFI", " F ", "IFI", 'I', Items.IRON_INGOT, 'F', Items.FEATHER);
		addShapedRecipe(getResource("mob_grinding_utils:fan_upgrade_3"), getResource("mob_grinding_utils"), new ItemStack(ModItems.FAN_UPGRADE, 1, 2), "FIF", "IRI", "FIF", 'I', Items.IRON_INGOT, 'F', Items.FEATHER, 'R', Items.REDSTONE);

		//Mob Swab
		addShapedRecipe(getResource("mob_grinding_utils:mob_swab"), getResource("mob_grinding_utils"), new ItemStack(ModItems.MOB_SWAB), "  W", " S ", "W  ", 'W', Blocks.WOOL, 'S', Items.STICK);

		//Wither Muffler 
		addShapedRecipe(getResource("mob_grinding_utils:wither_muffler"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.WITHER_MUFFLER), "WWW", "WSW", "WWW", 'W', Blocks.WOOL, 'S', new ItemStack(Items.SKULL, 1, 1));

		//Dragon Muffler
		addShapedRecipe(getResource("mob_grinding_utils:dragon_muffler"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.DRAGON_MUFFLER), "WWW", "WEW", "WWW", 'W', Blocks.WOOL, 'E', Blocks.DRAGON_EGG);

		//Mob Masher
		addShapedRecipe(getResource("mob_grinding_utils:saw"), getResource("mob_grinding_utils"), new ItemStack(ModBlocks.SAW), "SDS", "VRV", "DID", 'S', Items.IRON_SWORD, 'D', Items.DIAMOND, 'V', ModBlocks.SPIKES, 'R', Blocks.REDSTONE_BLOCK, 'I', Blocks.IRON_BLOCK);

		//Mob Masher Upgrades
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_1"), getResource("mob_grinding_utils"), new ItemStack(ModItems.SAW_UPGRADE, 1, 0), "GSG", "SRS", "GSG", 'G', Items.GOLD_NUGGET, 'S', Items.IRON_SWORD, 'R', Items.REDSTONE);
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_2"), getResource("mob_grinding_utils"), new ItemStack(ModItems.SAW_UPGRADE, 1, 1), "GLG", "LRL", "GLG", 'G', Items.GOLD_NUGGET, 'L', new ItemStack(Items.DYE, 1, 11), 'R', Items.REDSTONE);
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_3"), getResource("mob_grinding_utils"), new ItemStack(ModItems.SAW_UPGRADE, 1, 2), "GFG", "FRF", "GFG", 'G', Items.GOLD_NUGGET, 'F', Items.FLINT_AND_STEEL, 'R', Items.REDSTONE); 
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_4"), getResource("mob_grinding_utils"), new ItemStack(ModItems.SAW_UPGRADE, 1, 3), "GFG", "FRF", "GFG", 'G', Items.GOLD_NUGGET, 'F', Items.ROTTEN_FLESH, 'R', Items.REDSTONE);
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_5"), getResource("mob_grinding_utils"), new ItemStack(ModItems.SAW_UPGRADE, 1, 4), "GSG", "SRS", "GSG", 'G', Items.GOLD_NUGGET, 'S', Items.SPIDER_EYE, 'R', Items.REDSTONE);
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_6"), getResource("mob_grinding_utils"), new ItemStack(ModItems.SAW_UPGRADE, 1, 5), "GHG", "IRI", "GHG", 'G', Items.GOLD_NUGGET, 'H', Items.GOLDEN_HELMET, 'I', Items.IRON_HELMET, 'R', Items.REDSTONE);
	}

	private static void addShapelessRecipe(ResourceLocation name, ResourceLocation group, ItemStack output, Ingredient... params) {
		GameRegistry.addShapelessRecipe(name, group, output, params);
	}

	private static void addShapedRecipe(ResourceLocation name, ResourceLocation group, ItemStack output, Object... params) {
		GameRegistry.addShapedRecipe(name, group, output, params);
	}

	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation(inName);
	}
}