package mob_grinding_utils.recipe;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {

	public static IRecipe
	CHICKEN_FEED, ABSORPTION_HOPPER, ABSORPTION_UPGRADE, SPIKES, TANK, TANK_SINK, XP_TAP, FAN, FAN_UPGRADE_1,
	FAN_UPGRADE_2, FAN_UPGRADE_3, MOB_SWAB, WITHER_MUFFLER, DRAGON_MUFFLER, SAW, SAW_UPGRADE_1, SAW_UPGRADE_2,
	SAW_UPGRADE_3, SAW_UPGRADE_4, SAW_UPGRADE_5, SAW_UPGRADE_6;

	public static void init() {

		//Chicken Feed
		CHICKEN_FEED = new RecipeChickenFeed().setRegistryName(getResource("mob_grinding_utils:chicken_feed"));

		//Absorption Hopper
		ABSORPTION_HOPPER = new ShapedOreRecipe(getResource("recipe_absorption_hopper"), new ItemStack(ModBlocks.ABSORPTION_HOPPER_ITEM), " E ", " O ", "OHO", 'E', new ItemStack(Items.ENDER_EYE), 'O', "obsidian", 'H', new ItemStack(Blocks.HOPPER));
		ABSORPTION_HOPPER.setRegistryName(getResource("mob_grinding_utils:absorption_hopper"));

		//Absorption Hopper Upgrade
		ABSORPTION_UPGRADE = new ShapedOreRecipe(getResource("recipe_absorption_upgrade"), new ItemStack(ModItems.ABSORPTION_UPGRADE), " E ", "ERE", "OEO", 'E', "enderpearl", 'O', "obsidian", 'R', "dustRedstone");
		ABSORPTION_UPGRADE.setRegistryName(getResource("mob_grinding_utils:absorption_upgrade"));

		//Spikes
		SPIKES = new ShapedOreRecipe(getResource("recipe_spikes"), new ItemStack(ModBlocks.SPIKES_ITEM), "   ", " S ", "SIS", 'S', new ItemStack(Items.IRON_SWORD), 'I', "blockIron");
		SPIKES.setRegistryName(getResource("mob_grinding_utils:spikes"));

		//Tank
		TANK = new ShapedOreRecipe(getResource("recipe_tank"), new ItemStack(ModBlocks.TANK_ITEM), "IGI", "GGG", "IGI", 'I', "ingotIron", 'G', "blockGlass");
		TANK.setRegistryName(getResource("mob_grinding_utils:tank"));

		//Tank Sink
		TANK_SINK = new ShapedOreRecipe(getResource("recipe_tank_sink"), new ItemStack(ModBlocks.TANK_SINK_ITEM), " I ", "EHE", " T ", 'I', new ItemStack(Blocks.IRON_BARS), 'H', new ItemStack(Blocks.HOPPER), 'T', new ItemStack(ModBlocks.TANK_ITEM), 'E', new ItemStack(Items.ENDER_EYE));
		TANK_SINK.setRegistryName(getResource("mob_grinding_utils:tank_sink"));

		//XP TAP
		XP_TAP = new ShapedOreRecipe(getResource("recipe_xp_tap"), new ItemStack(ModBlocks.XP_TAP_ITEM), "O  ", "II ", "I  ", 'O', "obsidian", 'I', "ingotIron");
		XP_TAP.setRegistryName(getResource("mob_grinding_utils:xp_tap"));

		//Fan
		FAN = new ShapedOreRecipe(getResource("recipe_fan"), new ItemStack(ModBlocks.FAN_ITEM), "SIS", "IRI", "SIS", 'S', new ItemStack(Blocks.STONE_SLAB), 'I', "ingotIron", 'R', "blockRedstone");
		FAN.setRegistryName(getResource("mob_grinding_utils:fan"));

		//Fan Upgrades
		FAN_UPGRADE_1 = new ShapedOreRecipe(getResource("recipe_fan_uprade_1"), new ItemStack(ModItems.FAN_UPGRADE, 1, 0), "I I", "FFF", "I I", 'I', "ingotIron", 'F', "feather");
		FAN_UPGRADE_1.setRegistryName(getResource("mob_grinding_utils:fan_uprade_1"));

		FAN_UPGRADE_2 = new ShapedOreRecipe(getResource("recipe_fan_uprade_2"), new ItemStack(ModItems.FAN_UPGRADE, 1, 1), "IFI", " F ", "IFI", 'I', "ingotIron", 'F', "feather");
		FAN_UPGRADE_2.setRegistryName(getResource("mob_grinding_utils:fan_uprade_2"));

		FAN_UPGRADE_3 = new ShapedOreRecipe(getResource("recipe_fan_uprade_3"), new ItemStack(ModItems.FAN_UPGRADE, 1, 2), "FIF", "IRI", "FIF", 'I', "ingotIron", 'F', "feather", 'R', "dustRedstone");
		FAN_UPGRADE_3.setRegistryName(getResource("mob_grinding_utils:fan_uprade_3"));

		//Mob Swab
		MOB_SWAB = new ShapedOreRecipe(getResource("recipe_mob_swab"), new ItemStack(ModItems.MOB_SWAB), "  W", " S ", "W  ", 'W', new ItemStack(Blocks.WOOL), 'S', "stickWood");
		MOB_SWAB.setRegistryName(getResource("mob_grinding_utils:mob_swab"));

		//Wither Muffler 
		WITHER_MUFFLER = new ShapedOreRecipe(getResource("recipe_wither_muffler"), new ItemStack(ModBlocks.WITHER_MUFFLER_ITEM), "WWW", "WSW", "WWW", 'W', new ItemStack(Blocks.WOOL), 'S', new ItemStack(Items.SKULL, 1, 1));
		WITHER_MUFFLER.setRegistryName(getResource("mob_grinding_utils:wither_muffler"));

		//Dragon Muffler
		DRAGON_MUFFLER = new ShapedOreRecipe(getResource("recipe_dragon_muffler"), new ItemStack(ModBlocks.DRAGON_MUFFLER_ITEM), "WWW", "WEW", "WWW", 'W', new ItemStack(Blocks.WOOL), 'E', new ItemStack(Blocks.DRAGON_EGG));
		DRAGON_MUFFLER.setRegistryName(getResource("mob_grinding_utils:dragon_muffler"));

		//Mob Masher
		SAW = new ShapedOreRecipe(getResource("recipe_saw"), new ItemStack(ModBlocks.SAW_ITEM), "SDS", "VRV", "DID", 'S', new ItemStack(Items.IRON_SWORD), 'D', "gemDiamond", 'V', new ItemStack(ModBlocks.SPIKES_ITEM), 'R', "blockRedstone", 'I', "blockIron");
		SAW.setRegistryName(getResource("mob_grinding_utils:saw"));

		//Mob Masher Upgrades
		SAW_UPGRADE_1 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_1"), new ItemStack(ModItems.SAW_UPGRADE, 1, 0), "GSG", "SRS", "GSG", 'G', "nuggetGold", 'S', new ItemStack(Items.IRON_SWORD), 'R', "dustRedstone");
		SAW_UPGRADE_1.setRegistryName(getResource("mob_grinding_utils:saw_upgrade_1"));

		SAW_UPGRADE_2 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_2"), new ItemStack(ModItems.SAW_UPGRADE, 1, 1), "GLG", "LRL", "GLG", 'G', "nuggetGold", 'L', "dyeBlue", 'R', "dustRedstone");
		SAW_UPGRADE_2.setRegistryName(getResource("mob_grinding_utils:saw_upgrade_2"));

		SAW_UPGRADE_3 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_3"), new ItemStack(ModItems.SAW_UPGRADE, 1, 2), "GFG", "FRF", "GFG", 'G', "nuggetGold", 'F', new ItemStack(Items.FLINT_AND_STEEL), 'R', "dustRedstone"); 
		SAW_UPGRADE_3.setRegistryName(getResource("mob_grinding_utils:saw_upgrade_3"));

		SAW_UPGRADE_4 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_4"), new ItemStack(ModItems.SAW_UPGRADE, 1, 3), "GFG", "FRF", "GFG", 'G', "nuggetGold", 'F', new ItemStack(Items.ROTTEN_FLESH), 'R', "dustRedstone");
		SAW_UPGRADE_4.setRegistryName(getResource("mob_grinding_utils:saw_upgrade_4"));

		SAW_UPGRADE_5 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_5"), new ItemStack(ModItems.SAW_UPGRADE, 1, 4), "GSG", "SRS", "GSG", 'G', "nuggetGold", 'S', new ItemStack(Items.SPIDER_EYE), 'R', "dustRedstone");
		SAW_UPGRADE_5.setRegistryName(getResource("mob_grinding_utils:saw_upgrade_5"));

		SAW_UPGRADE_6 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_6"), new ItemStack(ModItems.SAW_UPGRADE, 1, 5), "GHG", "IRI", "GHG", 'G', "nuggetGold", 'H', new ItemStack(Items.GOLDEN_HELMET), 'I', new ItemStack(Items.IRON_HELMET), 'R', "dustRedstone");
		SAW_UPGRADE_6.setRegistryName(getResource("mob_grinding_utils:saw_upgrade_6"));
	}

	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation(inName);
	}

	@Mod.EventBusSubscriber(modid = "mob_grinding_utils")
	public static class RegistrationHandlerRecipes {
		@SubscribeEvent
		public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
			final IForgeRegistry<IRecipe> registry = event.getRegistry();
			registry.registerAll(
					CHICKEN_FEED,
					ABSORPTION_HOPPER,
					ABSORPTION_UPGRADE,
					SPIKES, TANK,
					TANK_SINK,
					XP_TAP,
					FAN,
					FAN_UPGRADE_1,
					FAN_UPGRADE_2,
					FAN_UPGRADE_3,
					MOB_SWAB,
					WITHER_MUFFLER,
					DRAGON_MUFFLER,
					SAW,
					SAW_UPGRADE_1,
					SAW_UPGRADE_2,
					SAW_UPGRADE_3,
					SAW_UPGRADE_4,
					SAW_UPGRADE_5,
					SAW_UPGRADE_6
					);
		}
	}

}