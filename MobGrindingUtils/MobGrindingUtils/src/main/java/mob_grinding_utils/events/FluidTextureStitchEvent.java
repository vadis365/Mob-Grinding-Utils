package mob_grinding_utils.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FluidTextureStitchEvent {
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	 public void onTextureStitchPre(TextureStitchEvent.Pre event) {
		event.addSprite(new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"));
		event.addSprite(new ResourceLocation("mob_grinding_utils:fluids/fluid_xp"));
	}

}