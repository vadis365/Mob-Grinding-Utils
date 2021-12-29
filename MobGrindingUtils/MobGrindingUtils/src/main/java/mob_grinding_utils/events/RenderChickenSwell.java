package mob_grinding_utils.events;

import mob_grinding_utils.models.ChickenBodyModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderChickenSwell {

	@SuppressWarnings("rawtypes")
	@SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
	@OnlyIn(Dist.CLIENT)
	public void renderChickenSwell(RenderLivingEvent.Post event) {
		if (event.getEntity() instanceof Chicken) {
			
			if (event.getEntity().getPersistentData().contains("shouldExplode")) {
				
				if (event.getRenderer().getModel() instanceof ChickenModel) {
					
					ChickenBodyModel model = new ChickenBodyModel();
					int count = event.getEntity().getPersistentData().getInt("countDown");
					float scale = count * 0.04F;
					if (scale >= 0.75F)
						scale = 0.75F;
					event.getMatrixStack().pushPose();
					event.getMatrixStack().translate(0D, - 0.5D - scale, 0D);
					event.getMatrixStack().mulPose(Vector3f.YN.rotationDegrees(event.getEntity().yBodyRot));
					event.getMatrixStack().scale(1F + scale, 1F + scale, 1F + scale * 0.75F);
					model.renderToBuffer(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.entitySolid(new ResourceLocation("textures/entity/chicken.png"))), event.getLight(), OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
					event.getMatrixStack().popPose();
				}
			}
		}
	}
}
