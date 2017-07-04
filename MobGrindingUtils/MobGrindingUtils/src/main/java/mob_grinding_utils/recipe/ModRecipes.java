package mob_grinding_utils.recipe;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {
	
	@Mod.EventBusSubscriber(modid = "mob_grinding_utils")
	public static class RegistrationHandlerItems {
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<IRecipe> event) {
		final IForgeRegistry<IRecipe> registry = event.getRegistry();
		registry.register(new RecipeChickenFeed().setRegistryName(new ResourceLocation("mob_grinding_utils", "recipe_chicken_feed")));
		RecipeSorter.register("mob_grinding_utils:recipe_chicken_feed", RecipeChickenFeed.class, RecipeSorter.Category.SHAPELESS, "");
		}
	}

	public static void addRecipes() {

		//Absorption Hopper
		addShapedRecipe(getResource("mob_grinding_utils:absorption_hopper"), getResource("recipe_absorption_hopper"), new ItemStack(ModBlocks.ABSORPTION_HOPPER_ITEM), " E ", " O ", "OHO", 'E', new ItemStack(Items.ENDER_EYE), 'O', new ItemStack(Blocks.OBSIDIAN), 'H', new ItemStack(Blocks.HOPPER));

		//Absorption Hopper Upgrade
		addShapedRecipe(getResource("mob_grinding_utils:absorption_upgrade"), getResource("recipe_absorption_upgrade"), new ItemStack(ModItems.ABSORPTION_UPGRADE), " E ", "ERE", "OEO", 'E', new ItemStack(Items.ENDER_PEARL), 'O', new ItemStack(Blocks.OBSIDIAN), 'R', new ItemStack(Items.REDSTONE));

		//Spikes
		addShapedRecipe(getResource("mob_grinding_utils:spikes"), getResource("recipe_spikes"), new ItemStack(ModBlocks.SPIKES_ITEM), "   ", " S ", "SIS", 'S', new ItemStack(Items.IRON_SWORD), 'I', new ItemStack(Blocks.IRON_BLOCK));

		//Tank
		addShapedRecipe(getResource("mob_grinding_utils:tank"), getResource("recipe_tank"), new ItemStack(ModBlocks.TANK_ITEM), "IGI", "GGG", "IGI", 'I', new ItemStack(Items.IRON_INGOT), 'G', new ItemStack(Blocks.GLASS));
		
		//Tank Sink
		addShapedRecipe(getResource("mob_grinding_utils:tank_sink"), getResource("recipe_tank_sink"), new ItemStack(ModBlocks.TANK_SINK_ITEM), " I ", "EHE", " T ", 'I', new ItemStack(Blocks.IRON_BARS), 'H', new ItemStack(Blocks.HOPPER), 'T', new ItemStack(ModBlocks.TANK_ITEM), 'E', new ItemStack(Items.ENDER_EYE));

		//XP TAP
		addShapedRecipe(getResource("mob_grinding_utils:xp_tap"), getResource("recipe_xp_tap"), new ItemStack(ModBlocks.XP_TAP_ITEM), "O  ", "II ", "I  ", 'O', new ItemStack(Blocks.OBSIDIAN), 'I', new ItemStack(Items.IRON_INGOT));

		//Fan
		addShapedRecipe(getResource("mob_grinding_utils:fan"), getResource("recipe_fan"), new ItemStack(ModBlocks.FAN_ITEM), "SIS", "IRI", "SIS", 'S', new ItemStack(Blocks.STONE_SLAB), 'I', new ItemStack(Items.IRON_INGOT), 'R', new ItemStack(Blocks.REDSTONE_BLOCK));

		//Fan Upgrades
		addShapedRecipe(getResource("mob_grinding_utils:fan_upgrade_1"), getResource("recipe_fan_upgrade_1"), new ItemStack(ModItems.FAN_UPGRADE, 1, 0), "I I", "FFF", "I I", 'I', new ItemStack(Items.IRON_INGOT), 'F', new ItemStack(Items.FEATHER));
		addShapedRecipe(getResource("mob_grinding_utils:fan_upgrade_2"), getResource("recipe_fan_upgrade_2"), new ItemStack(ModItems.FAN_UPGRADE, 1, 1), "IFI", " F ", "IFI", 'I', new ItemStack(Items.IRON_INGOT), 'F', new ItemStack(Items.FEATHER));
		addShapedRecipe(getResource("mob_grinding_utils:fan_upgrade_3"), getResource("recipe_fan_upgrade_3"), new ItemStack(ModItems.FAN_UPGRADE, 1, 2), "FIF", "IRI", "FIF", 'I', new ItemStack(Items.IRON_INGOT), 'F', new ItemStack(Items.FEATHER), 'R', new ItemStack(Items.REDSTONE));

		//Mob Swab
		addShapedRecipe(getResource("mob_grinding_utils:mob_swab"), getResource("recipe_mob_swab"), new ItemStack(ModItems.MOB_SWAB), "  W", " S ", "W  ", 'W', new ItemStack(Blocks.WOOL), 'S', new ItemStack(Items.STICK));

		//Wither Muffler 
		addShapedRecipe(getResource("mob_grinding_utils:wither_muffler"), getResource("recipe_wither_muffler"), new ItemStack(ModBlocks.WITHER_MUFFLER_ITEM), "WWW", "WSW", "WWW", 'W', new ItemStack(Blocks.WOOL), 'S', new ItemStack(Items.SKULL, 1, 1));

		//Dragon Muffler
		addShapedRecipe(getResource("mob_grinding_utils:dragon_muffler"), getResource("recipe_dragon_muffler"), new ItemStack(ModBlocks.DRAGON_MUFFLER_ITEM), "WWW", "WEW", "WWW", 'W', new ItemStack(Blocks.WOOL), 'E', new ItemStack(Blocks.DRAGON_EGG));

		//Mob Masher
		addShapedRecipe(getResource("mob_grinding_utils:saw"), getResource("recipe_saw"), new ItemStack(ModBlocks.SAW_ITEM), "SDS", "VRV", "DID", 'S', new ItemStack(Items.IRON_SWORD), 'D', new ItemStack(Items.DIAMOND), 'V', new ItemStack(ModBlocks.SPIKES_ITEM), 'R', new ItemStack(Blocks.REDSTONE_BLOCK), 'I', new ItemStack(Blocks.IRON_BLOCK));

		//Mob Masher Upgrades
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_1"), getResource("recipe_saw_upgrade_1"), new ItemStack(ModItems.SAW_UPGRADE, 1, 0), "GSG", "SRS", "GSG", 'G', new ItemStack(Items.GOLD_NUGGET), 'S', new ItemStack(Items.IRON_SWORD), 'R', new ItemStack(Items.REDSTONE));
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_2"), getResource("recipe_saw_upgrade_2"), new ItemStack(ModItems.SAW_UPGRADE, 1, 1), "GLG", "LRL", "GLG", 'G', new ItemStack(Items.GOLD_NUGGET), 'L', new ItemStack(Items.DYE, 1, 4), 'R', new ItemStack(Items.REDSTONE));
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_3"), getResource("recipe_saw_upgrade_3"), new ItemStack(ModItems.SAW_UPGRADE, 1, 2), "GFG", "FRF", "GFG", 'G', new ItemStack(Items.GOLD_NUGGET), 'F', new ItemStack(Items.FLINT_AND_STEEL), 'R', new ItemStack(Items.REDSTONE)); 
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_4"), getResource("recipe_saw_upgrade_4"), new ItemStack(ModItems.SAW_UPGRADE, 1, 3), "GFG", "FRF", "GFG", 'G', new ItemStack(Items.GOLD_NUGGET), 'F', new ItemStack(Items.ROTTEN_FLESH), 'R', new ItemStack(Items.REDSTONE));
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_5"), getResource("recipe_saw_upgrade_5"), new ItemStack(ModItems.SAW_UPGRADE, 1, 4), "GSG", "SRS", "GSG", 'G', new ItemStack(Items.GOLD_NUGGET), 'S', new ItemStack(Items.SPIDER_EYE), 'R', new ItemStack(Items.REDSTONE));
		addShapedRecipe(getResource("mob_grinding_utils:saw_upgrade_6"), getResource("recipe_saw_upgrade_6"), new ItemStack(ModItems.SAW_UPGRADE, 1, 5), "GHG", "IRI", "GHG", 'G', new ItemStack(Items.GOLD_NUGGET), 'H', new ItemStack(Items.GOLDEN_HELMET), 'I', new ItemStack(Items.IRON_HELMET), 'R', new ItemStack(Items.REDSTONE));
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