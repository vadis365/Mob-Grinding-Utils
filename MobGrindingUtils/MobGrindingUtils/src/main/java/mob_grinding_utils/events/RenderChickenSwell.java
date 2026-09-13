package mob_grinding_utils.events;

import com.mojang.math.Axis;
import mob_grinding_utils.models.ChickenBodyModel;
import mob_grinding_utils.util.RL;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
public class RenderChickenSwell {
	public static final ContextKey<Integer> SWELL_COUNTDOWN = new ContextKey<>(RL.mgu("chicken_swell"));
	/** Fallback when variant data is missing; matches vanilla temperate adult chicken. */
	private static final Identifier FALLBACK_CHICKEN_TEXTURE = RL.mc("textures/entity/chicken/chicken_temperate.png");

	public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(ChickenRenderer.class, (entity, state) -> {
			if (!(entity instanceof Chicken chicken)) {
				return;
			}
			CompoundTag nbt = chicken.getPersistentData();
			if (nbt.contains("shouldExplode")) {
				state.setRenderData(SWELL_COUNTDOWN, nbt.getIntOr("countDown", 0));
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderChickenSwell(RenderLivingEvent.Post event) {
		LivingEntityRenderState state = event.getRenderState();
		if (state.entityType != EntityType.CHICKEN) {
			return;
		}

		Integer count = state.getRenderData(SWELL_COUNTDOWN);
		if (count == null) {
			return;
		}

		if (!(event.getRenderer() instanceof ChickenRenderer chickenRenderer)
				|| !(event.getRenderer().getModel() instanceof ChickenModel)
				|| !(state instanceof ChickenRenderState chickenState)) {
			return;
		}

		ModelPart tempPart = ChickenBodyModel.createBodyLayer().bakeRoot();
		ChickenBodyModel model = new ChickenBodyModel(tempPart);
		float scale = Math.min(count * 0.04F, 0.75F);
		Identifier texture = resolveChickenTexture(chickenRenderer, chickenState);
		var poseStack = event.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(0D, -0.5D - scale, 0D);
		poseStack.mulPose(Axis.YN.rotationDegrees(state.yRot));
		poseStack.scale(1F + scale, 1F + scale, 1F + scale * 0.75F);
		event.getSubmitNodeCollector().submitModel(
				model,
				Unit.INSTANCE,
				poseStack,
				RenderTypes.entitySolid(texture),
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				0,
				null);
		poseStack.popPose();
	}

	private static Identifier resolveChickenTexture(ChickenRenderer renderer, ChickenRenderState state) {
		return state.variant == null ? FALLBACK_CHICKEN_TEXTURE : renderer.getTextureLocation(state);
	}
}
