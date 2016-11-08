package mob_grinding_utils.events;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderChickenSwell {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
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
}