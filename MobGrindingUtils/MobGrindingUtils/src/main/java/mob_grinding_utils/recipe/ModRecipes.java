package mob_grinding_utils.recipe;

import mob_grinding_utils.Reference;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {
	public static RecipeChickenFeed CHICKEN_FEED = new RecipeChickenFeed(new ResourceLocation(Reference.MOD_ID, "crafting_special_chicken_feed"));
    
	@SubscribeEvent
    public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
      //TODO  event.getRegistry().register(CHICKEN_FEED.setRegistryName(new ResourceLocation(Reference.MOD_ID, "chicken_feed")));
    }
}