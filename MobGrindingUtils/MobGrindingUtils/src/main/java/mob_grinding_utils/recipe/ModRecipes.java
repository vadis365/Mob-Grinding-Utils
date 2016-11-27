package mob_grinding_utils.recipe;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

	public static void addRecipes() {

		//Absorption Hopper
		addShapedRecipe(new ItemStack(MobGrindingUtils.ABSORPTION_HOPPER), " E ", " O ", "OHO", 'E', Items.ENDER_EYE, 'O', "obsidian", 'H', Blocks.HOPPER);

		//Absorption Hopper Upgrade
		addShapedRecipe(new ItemStack(MobGrindingUtils.ABSORPTION_UPGRADE), " E ", "ERE", "OEO", 'E', "enderpearl", 'O', "obsidian", 'R', "dustRedstone");

		//Spikes
		addShapedRecipe(new ItemStack(MobGrindingUtils.SPIKES), "   ", " S ", "SIS", 'S', Items.IRON_SWORD, 'I', "blockIron");

		//Tank
		addShapedRecipe(new ItemStack(MobGrindingUtils.TANK), "IGI", "GGG", "IGI", 'I', "ingotIron", 'G', "blockGlass");

		//XP TAP
		addShapedRecipe(new ItemStack(MobGrindingUtils.XP_TAP), "O  ", "II ", "I  ", 'O', "obsidian", 'I', "ingotIron");

		//Fan
		addShapedRecipe(new ItemStack(MobGrindingUtils.FAN), "SIS", "IRI", "SIS", 'S', Blocks.STONE_SLAB, 'I', "ingotIron", 'R', "blockRedstone");

		//Fan Upgrades
		addShapedRecipe(new ItemStack(MobGrindingUtils.FAN_UPGRADE, 1, 0), "I I", "FFF", "I I", 'I', "ingotIron", 'F', "feather");
		addShapedRecipe(new ItemStack(MobGrindingUtils.FAN_UPGRADE, 1, 1), "IFI", " F ", "IFI", 'I', "ingotIron", 'F', "feather");
		addShapedRecipe(new ItemStack(MobGrindingUtils.FAN_UPGRADE, 1, 2), "FIF", "IRI", "FIF", 'I', "ingotIron", 'F', "feather", 'R', "dustRedstone");

		//Mob Swab
		addShapedRecipe(new ItemStack(MobGrindingUtils.MOB_SWAB), "  W", " S ", "W  ", 'W', Blocks.WOOL, 'S', "stickWood");

		//Wither Muffler 
		addShapedRecipe(new ItemStack(MobGrindingUtils.WITHER_MUFFLER), "WWW", "WSW", "WWW", 'W', Blocks.WOOL, 'S', new ItemStack(Items.SKULL, 1, 1));

		//Dragon Muffler
		addShapedRecipe(new ItemStack(MobGrindingUtils.DRAGON_MUFFLER), "WWW", "WEW", "WWW", 'W', Blocks.WOOL, 'E', Blocks.DRAGON_EGG);

		//Mob Masher
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW), "SDS", "VRV", "DID", 'S', Items.IRON_SWORD, 'D', "gemDiamond", 'V', MobGrindingUtils.SPIKES, 'R', "blockRedstone", 'I', "blockIron");

		//Mob Masher Upgrades
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 0), "GSG", "SRS", "GSG", 'G', "nuggetGold", 'S', Items.IRON_SWORD, 'R', "dustRedstone");
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 1), "GLG", "LRL", "GLG", 'G', "nuggetGold", 'L', "dyeBlue", 'R', "dustRedstone");
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 2), "GFG", "FRF", "GFG", 'G', "nuggetGold", 'F', Items.FLINT_AND_STEEL, 'R', "dustRedstone"); 
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 3), "GFG", "FRF", "GFG", 'G', "nuggetGold", 'F', Items.ROTTEN_FLESH, 'R', "dustRedstone");
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 4), "GSG", "SRS", "GSG", 'G', "nuggetGold", 'S', Items.SPIDER_EYE, 'R', "dustRedstone");
		addShapedRecipe(new ItemStack(MobGrindingUtils.SAW_UPGRADE, 1, 5), "GHG", "IRI", "GHG", 'G', "nuggetGold", 'H', Items.GOLDEN_HELMET, 'I', Items.IRON_HELMET, 'R', "dustRedstone");
	}

	private static void addShapelessRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, parameters));
	}

	private static void addShapedRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, parameters));
	}
}