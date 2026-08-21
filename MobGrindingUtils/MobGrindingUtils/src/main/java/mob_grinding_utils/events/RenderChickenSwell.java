package mob_grinding_utils.events;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import mob_grinding_utils.models.ChickenBodyModel;
import mob_grinding_utils.util.RL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

public class RenderChickenSwell {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@OnlyIn(Dist.CLIENT)
	public void renderChickenSwell(RenderLivingEvent.Post<?, ?, ?> event) {
		if (event.getRenderer().getModel() instanceof ChickenModel && event.getRenderState() instanceof ChickenRenderState state && Minecraft.getInstance().level != null) {
			Chicken chicken = Minecraft.getInstance().level.getEntitiesOfClass(Chicken.class, new AABB(state.x - 0.01, state.y - 0.01, state.z - 0.01, state.x + 0.01, state.y + 0.01, state.z + 0.01)).stream().findFirst().orElse(null);
			if (chicken != null && chicken.getPersistentData().contains("shouldExplode")) {
					ModelPart tempPart = ChickenBodyModel.createBodyLayer().bakeRoot();
					ChickenBodyModel model = new ChickenBodyModel(tempPart);
					int count = chicken.getPersistentData().getInt("countDown").orElse(0);
					float scale = count * 0.04F;
					if (scale >= 0.75F)
						scale = 0.75F;
					event.getPoseStack().pushPose();
					event.getPoseStack().translate(0D, - 0.5D - scale, 0D);
					event.getPoseStack().mulPose(Axis.YN.rotationDegrees(state.bodyRot));
					event.getPoseStack().scale(1F + scale, 1F + scale, 1F + scale * 0.75F);
					event.getSubmitNodeCollector().submitCustomGeometry(event.getPoseStack(), RenderTypes.entityTranslucent(RL.mc("textures/entity/chicken.png")), (pose, buffer) -> {
						PoseStack modelStack = new PoseStack();
						modelStack.last().set(pose);
						model.renderToBuffer(modelStack, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
					});
					event.getPoseStack().popPose();
				}
			}
		}
}
