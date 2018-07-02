package mob_grinding_utils.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {
	public static final List<IRecipe> RECIPES = new ArrayList<IRecipe>();

	// Chicken Feed
	public static final IRecipe CHICKEN_FEED = new RecipeChickenFeed();

	// Absorption Hopper
	public static final IRecipe ABSORPTION_HOPPER = new ShapedOreRecipe(getResource("recipe_absorption_hopper"), new ItemStack(ModBlocks.ABSORPTION_HOPPER_ITEM), " E ", " O ", "OHO", 'E', new ItemStack(Items.ENDER_EYE), 'O', "obsidian", 'H', new ItemStack(Item.getItemFromBlock(Blocks.HOPPER)));

	// Absorption Hopper Upgrade
	public static final IRecipe ABSORPTION_UPGRADE = new ShapedOreRecipe(getResource("recipe_absorption_upgrade"), new ItemStack(ModItems.ABSORPTION_UPGRADE), " E ", "ERE", "OEO", 'E', "enderpearl", 'O', "obsidian", 'R', "dustRedstone");

	// Spikes
	public static final IRecipe SPIKES = new ShapedOreRecipe(getResource("recipe_spikes"), new ItemStack(ModBlocks.SPIKES_ITEM), "   ", " S ", "SIS", 'S', new ItemStack(Items.IRON_SWORD), 'I', "blockIron");

	// Tank
	public static final IRecipe TANK = new ShapedOreRecipe(getResource("recipe_tank"), new ItemStack(ModBlocks.TANK_ITEM), "IGI", "GGG", "IGI", 'I', "ingotIron", 'G', "blockGlass");

	// Tank Sink
	public static final IRecipe TANK_SINK = new ShapedOreRecipe(getResource("recipe_tank_sink"), new ItemStack(ModBlocks.TANK_SINK_ITEM), " I ", "EHE", " T ", 'I', new ItemStack(Blocks.IRON_BARS), 'H', new ItemStack(Item.getItemFromBlock(Blocks.HOPPER)), 'T', new ItemStack(ModBlocks.TANK_ITEM), 'E', new ItemStack(Items.ENDER_EYE));

	// XP TAP
	public static final IRecipe XP_TAP = new ShapedOreRecipe(getResource("recipe_xp_tap"), new ItemStack(ModBlocks.XP_TAP_ITEM), "O  ", "II ", "I  ", 'O', "obsidian", 'I', "ingotIron");

	// Fan
	public static final IRecipe FAN = new ShapedOreRecipe(getResource("recipe_fan"), new ItemStack(ModBlocks.FAN_ITEM), "SIS", "IRI", "SIS", 'S', new ItemStack(Blocks.STONE_SLAB), 'I', "ingotIron", 'R', "blockRedstone");

	// Fan Upgrades
	public static final IRecipe FAN_UPGRADE_1 = new ShapedOreRecipe(getResource("recipe_fan_uprade_1"), new ItemStack(ModItems.FAN_UPGRADE, 1, 0), "I I", "FFF", "I I", 'I', "ingotIron", 'F', "feather");
	public static final IRecipe FAN_UPGRADE_2 = new ShapedOreRecipe(getResource("recipe_fan_uprade_2"), new ItemStack(ModItems.FAN_UPGRADE, 1, 1), "IFI", " F ", "IFI", 'I', "ingotIron", 'F', "feather");
	public static final IRecipe FAN_UPGRADE_3 = new ShapedOreRecipe(getResource("recipe_fan_uprade_3"), new ItemStack(ModItems.FAN_UPGRADE, 1, 2), "FIF", "IRI", "FIF", 'I', "ingotIron", 'F', "feather", 'R', "dustRedstone");

	// Mob Swab
	public static final IRecipe MOB_SWAB = new ShapedOreRecipe(getResource("recipe_mob_swab"), new ItemStack(ModItems.MOB_SWAB), "  W", " S ", "W  ", 'W', new ItemStack(Blocks.WOOL), 'S', "stickWood");

	// Wither Muffler
	public static final IRecipe WITHER_MUFFLER = new ShapedOreRecipe(getResource("recipe_wither_muffler"), new ItemStack(ModBlocks.WITHER_MUFFLER_ITEM), "WWW", "WSW", "WWW", 'W', new ItemStack(Blocks.WOOL), 'S', new ItemStack(Items.SKULL, 1, 1));

	// Dragon Muffler
	public static final IRecipe DRAGON_MUFFLER = new ShapedOreRecipe(getResource("recipe_dragon_muffler"), new ItemStack(ModBlocks.DRAGON_MUFFLER_ITEM), "WWW", "WEW", "WWW", 'W', new ItemStack(Blocks.WOOL), 'E', new ItemStack(Blocks.DRAGON_EGG));

	// Mob Masher
	public static final IRecipe SAW = new ShapedOreRecipe(getResource("recipe_saw"), new ItemStack(ModBlocks.SAW_ITEM), "SDS", "VRV", "DID", 'S', new ItemStack(Items.IRON_SWORD), 'D', "gemDiamond", 'V', new ItemStack(ModBlocks.SPIKES_ITEM), 'R', "blockRedstone", 'I', "blockIron");

	// Mob Masher Upgrades
	public static final IRecipe SAW_UPGRADE_1 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_1"), new ItemStack(ModItems.SAW_UPGRADE, 1, 0), "GSG", "SRS", "GSG", 'G', "nuggetGold", 'S', new ItemStack(Items.IRON_SWORD), 'R', "dustRedstone");
	public static final IRecipe SAW_UPGRADE_2 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_2"), new ItemStack(ModItems.SAW_UPGRADE, 1, 1), "GLG", "LRL", "GLG", 'G', "nuggetGold", 'L', "dyeBlue", 'R', "dustRedstone");
	public static final IRecipe SAW_UPGRADE_3 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_3"), new ItemStack(ModItems.SAW_UPGRADE, 1, 2), "GFG", "FRF", "GFG", 'G', "nuggetGold", 'F', new ItemStack(Items.FLINT_AND_STEEL), 'R', "dustRedstone");
	public static final IRecipe SAW_UPGRADE_4 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_4"), new ItemStack(ModItems.SAW_UPGRADE, 1, 3), "GFG", "FRF", "GFG", 'G', "nuggetGold", 'F', new ItemStack(Items.ROTTEN_FLESH), 'R', "dustRedstone");
	public static final IRecipe SAW_UPGRADE_5 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_5"), new ItemStack(ModItems.SAW_UPGRADE, 1, 4), "GSG", "SRS", "GSG", 'G', "nuggetGold", 'S', new ItemStack(Items.SPIDER_EYE), 'R', "dustRedstone");
	public static final IRecipe SAW_UPGRADE_6 = new ShapedOreRecipe(getResource("recipe_saw_upgrade_6"), new ItemStack(ModItems.SAW_UPGRADE, 1, 5), "GHG", "IRI", "GHG", 'G', "nuggetGold", 'H', new ItemStack(Items.GOLDEN_HELMET), 'I', new ItemStack(Items.IRON_HELMET), 'R', "dustRedstone");

	// Entity Conveyor
	public static final IRecipe ENTITY_CONVEYOR = new ShapedOreRecipe(getResource("recipe_entity_conveyor"), new ItemStack(ModBlocks.ENTITY_CONVEYOR_ITEM, 6, 0), " S ", "IRI", "ISI", 'I', "ingotIron", 'S', "slimeball", 'R', "dustRedstone");

	// Ender Inhibitor
	public static final IRecipe ENDER_INHIBITOR = new ShapedOreRecipe(getResource("recipe_ender_inhibitor"), new ItemStack(ModBlocks.ENDER_INHIBITOR_ON_ITEM, 1, 0), " R ", "IEI", " G ", 'I', "ingotIron", 'E', new ItemStack(Items.ENDER_EYE), 'R', "dustRedstone", 'G', "dustGlowstone");

	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation(Reference.MOD_ID, inName);
	}

	public static void init() {
		try {
			for (Field field : ModRecipes.class.getDeclaredFields()) {
				Object obj = field.get(null);
				if (obj instanceof IRecipe) {
					IRecipe recipe = (IRecipe) obj;
					String name = field.getName().toLowerCase(Locale.ENGLISH);
					registerRecipe(name, recipe);
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void registerRecipe(String inName, IRecipe recipe) {
		RECIPES.add(recipe);
		recipe.setRegistryName(getResource(inName));
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	public static class RegistrationHandlerRecipes {
		//@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
			init();
			final IForgeRegistry<IRecipe> registry = event.getRegistry();
			for (IRecipe recipes : RECIPES)
				registry.register(recipes);
		}
	}
}