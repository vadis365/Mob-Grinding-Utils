package mob_grinding_utils.events;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleTextureStitchEvent {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	 public void onTextureStitchPre(TextureStitchEvent.Pre event) {
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles0"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles1"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles2"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles3"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles4"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles5"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles6"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles7"));
	  event.addSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles8"));
	}

}
