package mob_grinding_utils.events;

import mob_grinding_utils.capability.bossbars.BossBarPlayerCapability;
import mob_grinding_utils.capability.bossbars.IBossBarCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.BossInfo;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandlers {

	//Boss Bar Hiding Event
	@SubscribeEvent
	public void onRenderHUD(BossInfo event) {
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.BOSSINFO)) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player != null) {
				IBossBarCapability cap = player.getCapability(BossBarPlayerCapability.CAPABILITY_PLAYER_BOSS_BAR, null);

				if (!cap.renderWitherBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Wither"))
						event.setCanceled(true);
				}

				if (!cap.renderEnderDragonBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Ender Dragon"))
						event.setCanceled(true);
				}
			}
		}
	}

	//Global Dragon Sound Event
	@SubscribeEvent
	public void onDragonSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.enderdragon.death"))
			event.setResultSound(null);
	}

	//Global Wither Boss Sound Event
	@SubscribeEvent
	public void onWitherSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.wither.spawn") || event.getName().equals("entity.wither.death"))
			event.setResultSound(null);
	}

	//Chicken Swell Event
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderChickenSwell(RenderLivingEvent.Post event) {
		if (event.getEntity() != null && event.getEntity() instanceof EntityChicken) {
			if (event.getEntity().getEntityData().hasKey("shouldExplode")) {
				ModelChicken model = (ModelChicken) event.getRenderer().getMainModel();
				int count = event.getEntity().getEntityData().getInteger("countDown");
				float scale = count * 0.04F;
				if (scale >= 0.75F)
					scale = 0.75F;
				GlStateManager.pushMatrix();
				GlStateManager.translate(event.getX(), event.getY() - 0.5D - scale, event.getZ());
				GlStateManager.rotate(event.getEntity().renderYawOffset, 0, -1, 0);
				GlStateManager.scale(1F + scale, 1F + scale, 1F + scale * 0.75F);
				model.body.render(0.0625F);
				GlStateManager.popMatrix();
			}
		}
	}

	//Texture Particles
	@SubscribeEvent
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
