package mob_grinding_utils.events;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderChickenSwell {

	@SuppressWarnings("rawtypes")
	@SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
	@OnlyIn(Dist.CLIENT)
	public void renderChickenSwell(RenderLivingEvent.Post event) {
		if (event.getEntity() != null && event.getEntity() instanceof ChickenEntity) {
			if (event.getEntity().getPersistentData().contains("shouldExplode")) {
				if (event.getRenderer().getEntityModel() instanceof ChickenModel) {
					ChickenModel model = (ChickenModel) event.getRenderer().getEntityModel();
					int count = event.getEntity().getPersistentData().getInt("countDown");
					float scale = count * 0.04F;
					if (scale >= 0.75F)
						scale = 0.75F;
					event.getMatrixStack().push();
					event.getMatrixStack().translate(event.getEntity().getPosX(), event.getEntity().getPosY() - 0.5D - scale, event.getEntity().getPosZ());
					event.getMatrixStack().rotate(Vector3f.YN.rotationDegrees(event.getEntity().renderYawOffset));
					event.getMatrixStack().scale(1F + scale, 1F + scale, 1F + scale * 0.75F);
					///model.body.render(0.0625F); hhhhhhhhnnnnnnnhhhhnnnnggggghhhhnn / will sort out later
					event.getMatrixStack().pop();
				}
			}
		}
	}
}