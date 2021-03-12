package mob_grinding_utils;

import mob_grinding_utils.recipe.RecipeChickenFeed;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {
	public static RecipeChickenFeed CHICKEN_FEED;

	private static void init() {
		CHICKEN_FEED = new RecipeChickenFeed(new ResourceLocation(Reference.MOD_ID, "chicken_feed_recipe"));
	}

	@SubscribeEvent
    public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
		init();
		event.getRegistry().register(CHICKEN_FEED.getSerializer().setRegistryName(new ResourceLocation(Reference.MOD_ID, "chicken_feed_recipe")));
    }

}