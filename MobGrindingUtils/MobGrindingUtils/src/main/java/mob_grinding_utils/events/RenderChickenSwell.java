package mob_grinding_utils.events;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderChickenSwell {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@OnlyIn(Dist.CLIENT)
	public void renderChickenSwell(RenderLivingEvent.Post event) {
		if (event.getEntity() != null && event.getEntity() instanceof EntityChicken) {
			if (event.getEntity().getEntityData().hasKey("shouldExplode")) {
				if (event.getRenderer().getMainModel() instanceof ModelChicken) {
					ModelChicken model = (ModelChicken) event.getRenderer().getMainModel();
					int count = event.getEntity().getEntityData().getInteger("countDown");
					float scale = count * 0.04F;
					if (scale >= 0.75F)
						scale = 0.75F;
					GlStateManager.pushMatrix();
					GlStateManager.translated(event.getX(), event.getY() - 0.5D - scale, event.getZ());
					GlStateManager.rotatef(event.getEntity().renderYawOffset, 0F, -1F, 0F);
					GlStateManager.scalef(1F + scale, 1F + scale, 1F + scale * 0.75F);
					model.body.render(0.0625F);
					GlStateManager.popMatrix();
				}
			}
		}
	}
}