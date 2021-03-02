package mob_grinding_utils.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.IRecipe;


public class ModRecipes {
	public static final List<IRecipe> RECIPES = new ArrayList<IRecipe>();

	// Chicken Feed
	public static final IRecipe CHICKEN_FEED = new RecipeChickenFeed();
	/*
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
	*/
}