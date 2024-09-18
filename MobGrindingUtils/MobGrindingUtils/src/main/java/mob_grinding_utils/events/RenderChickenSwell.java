package mob_grinding_utils.events;

import com.mojang.math.Axis;
import mob_grinding_utils.models.ChickenBodyModel;
import mob_grinding_utils.util.RL;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

public class RenderChickenSwell {

	@SuppressWarnings("rawtypes")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	@OnlyIn(Dist.CLIENT)
	public void renderChickenSwell(RenderLivingEvent.Post event) {
		if (event.getEntity() instanceof Chicken) {
			
			if (event.getEntity().getPersistentData().contains("shouldExplode")) {
				
				if (event.getRenderer().getModel() instanceof ChickenModel) {
					ModelPart tempPart = ChickenBodyModel.createBodyLayer().bakeRoot();
					ChickenBodyModel model = new ChickenBodyModel(tempPart); /// TODO errrrrr
					int count = event.getEntity().getPersistentData().getInt("countDown");
					float scale = count * 0.04F;
					if (scale >= 0.75F)
						scale = 0.75F;
					event.getPoseStack().pushPose();
					event.getPoseStack().translate(0D, - 0.5D - scale, 0D);
					event.getPoseStack().mulPose(Axis.YN.rotationDegrees(event.getEntity().yBodyRot));
					event.getPoseStack().scale(1F + scale, 1F + scale, 1F + scale * 0.75F);
					model.renderToBuffer(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.entitySolid(RL.mc("textures/entity/chicken.png"))), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
					event.getPoseStack().popPose();
				}
			}
		}
	}
}
