package mob_grinding_utils.events;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleTextureStitchEvent {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	 public void onTextureStitchPre(TextureStitchEvent.Pre event) {
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles0"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles1"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles2"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles3"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles4"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles5"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles6"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles7"));
	  event.getMap().registerSprite(new ResourceLocation("mob_grinding_utils:particles/fluid_xp_particles8"));
	}

}
